package com.dcj.meeting.pojo.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server.open-properties")
public class OpenProperties {
    private double startHour = 8.0;  //开放时间
    private double closeHour = 22.0; //关闭时间

    public double getStartHour() {
        return startHour;
    }

    public void setStartHour(double startHour) {
        this.startHour = startHour;
    }

    public double getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(double closeHour) {
        this.closeHour = closeHour;
    }

    public static boolean invalidOpenTime(int sh, int sm, int ch, int cm) {
        if (sh > ch) {
            return false;
        }
        if (sh < 0 || sh > 23 || ch < 0 || ch > 23) {
            return false;
        }
        if (sm < 0 || sm > 59 || cm < 0 || cm > 59) {
            return false;
        }
        return true;
    }
}
