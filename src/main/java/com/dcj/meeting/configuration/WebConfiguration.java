package com.dcj.meeting.configuration;

import com.dcj.meeting.pojo.enterprise.AccessToken;
import com.dcj.meeting.pojo.enterprise.AppProperties;
import com.dcj.meeting.pojo.server.AutoCheckProperties;
import com.dcj.meeting.pojo.server.AutoCleanProperties;
import com.dcj.meeting.service.AppointmentService;
import com.dcj.meeting.service.CheckService;
import com.dcj.meeting.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@Configuration
public class WebConfiguration {

    @Autowired
    AppProperties appProperties;
    @Autowired
    AccessToken accessToken;
    @Autowired
    AutoCheckProperties autoCheckProperties;
    @Autowired
    AutoCleanProperties autoCleanProperties;
    @Autowired
    CheckService checkService;
    @Autowired
    AppointmentService appointmentService;

    @Bean
    public ServletRegistrationBean<HttpServlet> threadServlet() {
        HttpServlet servlet = new HttpServlet() {
            @Override
            public void init() throws ServletException {
                super.init();
                //开启获取AccessToken线程
                new Thread(new AccessTokenThread(), "accessToken线程").start();
                //开启自动审核预约线程
                new Thread(new AutoCheckThread(), "自动审核预约线程").start();
                //开启检出过期记录线程
                new Thread(new MakeOutOfDateThread(), "检出过期记录线程").start();
                //开启自动清理过期记录线程
                new Thread(new AutoCleanThread(), "清理过期记录线程").start();
            }
        };
        ServletRegistrationBean<HttpServlet> registrationBean = new ServletRegistrationBean<>(servlet, "/thread");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }

    /**
     * 获取AccessToken线程
     */
    public class AccessTokenThread implements Runnable {

        @Override
        public void run() {
            Logger logger = LoggerFactory.getLogger(WebConfiguration.class);
            try {
                while (true) {
                    AccessToken newAccessToken = AuthUtil.getAccessToken(appProperties.getCorpid(), appProperties.getSecret());
                    if (newAccessToken != null && newAccessToken.getAccess_token() != null) {
                        LoggerFactory.getLogger(WebConfiguration.class).info("access_token获取成功：" + newAccessToken.getAccess_token());
                        accessToken.setAccess_token(newAccessToken.getAccess_token());
                        accessToken.setExpires_in(newAccessToken.getExpires_in());
                        accessToken.setExpireTime(newAccessToken.getExpireTime());
                        Thread.sleep(newAccessToken.getExpires_in() * 1000 - 300000);
                    } else {
                        //获取accessToken发生异常，休眠10秒
                        Thread.sleep(10 * 1000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自动审核预约线程
     */
    public class AutoCheckThread implements Runnable {

        @Override
        public void run() {
            Logger logger = LoggerFactory.getLogger(WebConfiguration.class);
            try {
                //等待项目启动获取accessToken(发送审核通知需要accessToken)
                while (accessToken.getAccess_token() == null) {
                    Thread.sleep(1000);
                }
                logger.info("自动审核线程启动，自动审核启用状态：" + (autoCheckProperties.isEnabled() ? "是" : "否"));
                while (true) {
                    if (autoCheckProperties.isEnabled()) {
                        int i = checkService.approveAll("自动审核");
                        if (i > 0) {
                            logger.info("自动审核...审核预约条数：" + i);
                        }
                        //节省系统开销，休眠一段时间再审核
                        Thread.sleep(autoCheckProperties.getCheckInterval() * 60 * 1000);
                    } else {
                        //自动审核关闭，休眠一段时间后，再判断功能是否再次被启用
                        Thread.sleep(autoCheckProperties.getSleepTime());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检出过期记录线程
     */
    public class MakeOutOfDateThread implements Runnable {

        @Override
        public void run() {
            Logger logger = LoggerFactory.getLogger(WebConfiguration.class);
            try {
                while (true) {
                    //每隔一分钟检出过期记录
                    appointmentService.makeOutOfDate();
                    Thread.sleep(60 * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清理过期记录线程
     */
    public class AutoCleanThread implements Runnable {

        @Override
        public void run() {
            Logger logger = LoggerFactory.getLogger(WebConfiguration.class);
            try {
                while (true) {
                    if (autoCleanProperties.isEnabled()) {
                        int index = appointmentService.cleanOutOfDate();
                        logger.info("清理过期记录：" + index + "条");
                    }
                    Thread.sleep(autoCleanProperties.getCleanInterval() * 24 * 60 * 60 * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
