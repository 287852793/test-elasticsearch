package com.example.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.demo.config.EsIndexConfig;
import com.example.demo.model.Article;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private final ElasticsearchClient esClient;

    public ArticleService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    // ① 创建文档
    public String addArticle(Article article) throws IOException {
        // 如果文章没有设置创建时间，则设置为当前时间
        if (article.getCreatedAt() == null || article.getCreatedAt().isEmpty()) {
            article.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        
        IndexResponse response = esClient.index(i -> i
                .index(EsIndexConfig.INDEX_NAME)    // 使用统一的索引名
                .id(article.getId())
                .document(article)
        );
        return response.id();
    }

    // ② 按关键字搜索
    public List<Article> search(String keyword) throws IOException {
        SearchResponse<Article> response = esClient.search(s -> s
                        .index(EsIndexConfig.INDEX_NAME)  // 使用统一的索引名
                        .query(q -> q
                                .multiMatch(m -> m
                                        .fields("title", "content")
                                        .query(keyword)
                                )
                        ),
                Article.class);

        List<Article> result = new ArrayList<>();
        for (Hit<Article> hit : response.hits().hits()) {
            result.add(hit.source());
        }
        return result;
    }
}