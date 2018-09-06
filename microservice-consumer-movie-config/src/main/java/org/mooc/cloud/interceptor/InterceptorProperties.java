package org.mooc.cloud.interceptor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author : henry
 * @version : 1.0
 * @Description :
 * @Copyright : Sinaif All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司-富鱼
 * @Create Date : 2018/9/6 11:02
 */

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
