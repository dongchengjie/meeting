package com.dcj.meeting.pojo.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server.auto-check-properties")
public class AutoCheckProperties {
    private boolean enabled = true;     //是否启用自动审核（默认启用）
    private int checkInterval = 2;      //自动审核间隔（默认间隔2分钟）
    private int sleepTime = 3000;       //关闭自动审核后，休眠时间

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
