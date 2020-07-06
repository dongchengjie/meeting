package com.dcj.meeting.pojo.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server.auto-clean-properties")
public class AutoCleanProperties {
    private boolean enabled = true;    //是否启用自动清理过期记录
    private int cleanInterval = 7;     //清理周期（单位天）

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCleanInterval() {
        return cleanInterval;
    }

    public void setCleanInterval(int cleanInterval) {
        this.cleanInterval = cleanInterval;
    }
}
