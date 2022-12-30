package com.gemini.Contripoint.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 20/08/20
 * <p>
 * Used in retrieving data from mis api
 * Reference in controller.UserController
 */

@ConfigurationProperties(prefix = "mis.data")
@Configuration("misData")
@Getter
@Setter
public class MisData {
    private String url;
    private String token;
    private String key;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}