//package com.lowi.admin.config;
//
//import org.elasticsearch.client.Client;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//
//import java.net.InetAddress;
//
///**
// * com.lyx.ucenter.controller.DemoController.java
// * ==============================================
// * Copy right 2015-2017 by http://www.51lick.com
// * ----------------------------------------------
// * This is not a free software, without any authorization is not allowed to use and spread.
// * ==============================================
// *
// * @author : qinxh
// * @version : v2.0
// * @desc :ElasticSearch客户端配置类
// * @since : 2019-10-16 09:47
// */
//@Configuration
//public class ElasticSearchConfig {
//    private Logger logger= LoggerFactory.getLogger(ElasticSearchConfig.class);
//    @Value("${elasticsearchServerUrl}")
//    private String elasticsearchServerUrl; //ES服务地址
//    @Value("${elasticsearchServerPort}")
//    private Integer elasticsearchServerPort; //ES服务地址
//
//
//    @Bean
//    public Client client() throws Exception{
//        Settings esSetting = Settings.builder().build();
//        return new PreBuiltTransportClient(esSetting).addTransportAddress(new TransportAddress(InetAddress.getByName(elasticsearchServerUrl),elasticsearchServerPort));
//    }
//    @Bean(name = "elasticsearchTemplate")
//    public ElasticsearchOperations elasticsearchOperations() throws Exception{
//        ElasticsearchTemplate elasticsearchTemplate;
//        try {
//            elasticsearchTemplate = new ElasticsearchTemplate(client());
//            return elasticsearchTemplate;
//        } catch (Exception e) {
//            logger.error("初始化ElasticsearchTemplate失败");
//            return new ElasticsearchTemplate(client());
//        }
//    }
//}
