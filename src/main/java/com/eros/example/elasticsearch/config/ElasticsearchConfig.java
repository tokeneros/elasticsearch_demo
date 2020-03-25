package com.eros.example.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/13 11:12
 * @Version: 1.0
 * @Modified By:
 */
@Component
public class ElasticsearchConfig {

    private static String INET_ADDRESS = "localhost";
//    private static String INET_ADDRESS = "proxy.imeyun.com";

    private static Integer PORT = 9300;
//    private static Integer PORT = 9006;

    private static String CLUSTER_NAME_KEY = "cluster.name";

    private static String CLUSTER_NAME_VAL = "es643";
//    private static String CLUSTER_NAME_VAL = "docker-cluster";

    @Bean
    public TransportClient transportClient(){
        TransportClient client = null;
        try {
//            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("localhost"), Integer.valueOf("9300"));
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(INET_ADDRESS), PORT);
            // 配置信息
            Settings settings = Settings.builder()
                    .put(CLUSTER_NAME_KEY, CLUSTER_NAME_VAL)
                    .put("thread_pool.search.size", 5)// 增加线程池个数，暂时设为5
                    .put("thread_pool.bulk.size", 5)
                    .build();
            client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(transportAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

//    @Bean
//    RestHighLevelClient elasticsearchClient() {
//        ClientConfiguration configuration = ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .build();
//        RestHighLevelClient client = RestClients.create(configuration).rest();
//        return client;
//    }

}
