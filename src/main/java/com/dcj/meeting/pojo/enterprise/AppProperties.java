package com.dcj.meeting.pojo.enterprise;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "enterprise.app-properties")
public class AppProperties {
    private String corpid;
    private String agentid;
    private String secret;

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "AppProperties{" +
                "corpid='" + corpid + '\'' +
                ", agentid='" + agentid + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
