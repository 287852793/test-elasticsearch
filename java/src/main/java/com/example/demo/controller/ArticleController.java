package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    // 添加文章
    @PostMapping("/add")
    public String add(@RequestBody Article article) throws IOException {
        return service.addArticle(article);
    }

    // 搜索文章
    @GetMapping("/search")
    public List<Article> search(@RequestParam String keyword) throws IOException {
        return service.search(keyword);
    }
}
