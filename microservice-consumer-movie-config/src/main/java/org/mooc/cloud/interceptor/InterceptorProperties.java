package org.mooc.cloud.interceptor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "interceptor")
public class InterceptorProperties {

    private List<String> includePattens;

    private List<String> excludePattens;

    public List<String> getIncludePattens() {
        return includePattens;
    }

    public void setIncludePattens(List<String> includePattens) {
        this.includePattens = includePattens;
    }

    public List<String> getExcludePattens() {
        return excludePattens;
    }

    public void setExcludePattens(List<String> excludePattens) {
        this.excludePattens = excludePattens;
    }
}
