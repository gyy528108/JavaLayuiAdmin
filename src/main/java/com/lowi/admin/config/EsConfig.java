package com.lowi.admin.config;


import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * EsConfig.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/27 14:07
 */
@Configuration
public class EsConfig {


    @Value("${elasticsearchServerUrl}")
    private String elasticsearchServerUrl; //ES服务地址
    @Value("${elasticsearchServerPort}")
    private Integer elasticsearchServerPort; //ES服务地址

    private boolean uniqueConnectTimeConfig = true;
    private RestClientBuilder builder;
    private RestHighLevelClient client;

    /**
     * 返回一个RestHighLevelClient
     *
     * @return
     */
    @Bean(autowire = Autowire.BY_NAME, name = "restHighLevelClient")
    public RestHighLevelClient client() {
        builder = RestClient.builder(new HttpHost(elasticsearchServerUrl, elasticsearchServerPort));
        if (uniqueConnectTimeConfig) {
            setConnectTimeOutConfig();
        }
        client = new RestHighLevelClient(builder);
        return client;
    }

    /**
     * 异步httpclient的连接延时配置
     */
    public void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(5000);
                requestConfigBuilder.setSocketTimeout(60000);
                return requestConfigBuilder;
            }
        });
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
