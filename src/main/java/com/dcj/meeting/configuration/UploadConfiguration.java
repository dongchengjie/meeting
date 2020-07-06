package com.dcj.meeting.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Configuration
public class UploadConfiguration {
    //类路径根目录
    @Bean
    public File rootPath() {
        File rootPath = null;
        try {
            rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return rootPath;
    }
}
