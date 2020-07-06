package com.dcj.meeting.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class HttpUtil {

    private static SimpleClientHttpRequestFactory requestFactory;

    public static String get(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(getClientHttpRequestFactory());
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, new HashMap<>());
        return result.getBody();
    }

    public static String postJson(String url, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        //发送post请求
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(getClientHttpRequestFactory());
        return restTemplate.postForObject(url, entity, String.class);
    }

    private static ClientHttpRequestFactory getClientHttpRequestFactory() {
        if (requestFactory == null) {
            requestFactory = new SimpleClientHttpRequestFactory();
        }
        // 设置超时
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        return requestFactory;
    }
}
