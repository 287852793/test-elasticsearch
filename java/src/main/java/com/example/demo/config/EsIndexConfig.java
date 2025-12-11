package com.example.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EsIndexConfig {

    private static final Logger logger = LoggerFactory.getLogger(EsIndexConfig.class);

    private final ElasticsearchClient esClient;

    // 索引名
    public static final String INDEX_NAME = "my_index";

    @PostConstruct
    public void init() {
        try {
            createIndexIfNotExists();
        } catch (Exception e) {
            logger.error("启动时创建 Elasticsearch 索引失败", e);
            throw new RuntimeException("启动时创建 Elasticsearch 索引失败", e);
        }
    }

    private void createIndexIfNotExists() throws Exception {
        // 先判断索引是否存在
        boolean exists = esClient.indices()
                .exists(ExistsRequest.of(e -> e.index(INDEX_NAME)))
                .value();

        if (exists) {
            logger.info("ES 索引已存在：" + INDEX_NAME);
            return;
        }

        logger.info("ES 索引不存在，将自动创建：" + INDEX_NAME);

        // 创建索引，并定义简单 mapping & settings
        esClient.indices().create(c -> c
                .index(INDEX_NAME)
                .settings(s -> s
                        .numberOfShards("1")
                        .numberOfReplicas("1")
                )
                .mappings(m -> m
                        .properties("title", p -> p.text(t -> t))
                        .properties("content", p -> p.text(t -> t))
                        .properties("createdAt", p -> p.date(d -> d))
                )
        );

        logger.info("ES 索引创建成功：" + INDEX_NAME);
    }
}