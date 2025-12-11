# test-elasticsearch

#### NOTE
es7 支持 jdk 8  
es8 最低需要 jdk 17  
两者的 java 依赖和代码实现有较大差别，需要注意
以下内容基于 es8 进行测试

#### 启动服务容器
启动容器时，如果 es 挂载的本地路径如：
```
./volumes/es/data:/usr/share/elasticsearch/data
```
会出现权限不够而报错，这种情况下，需要执行如下代码
```
chown -R 1000:1000 ./volumes/es/data
```
然后再重新启动容器即可

或者直接将数据挂载到 docker driver 中，可避免权限问题

#### 正常启动容器之后
验证 ES 和 Kibana
1. 浏览器访问 ES

浏览器打开：

http://localhost:9200


你应该看到类似：
```json
{
  "name" : "es",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "...",
  "version" : {
    "number" : "8.13.2"
  },
  "tagline" : "You Know, for Search"
}
```

2. 打开 Kibana

打开：

http://localhost:5601


出现 Kibana 首页，代表安装成功。

3. 在 Kibana Console 执行一个基本命令

在 Kibana 左侧菜单 → Dev Tools → Console
输入：
```
GET _cat/indices?v
```

能返回空列表说明正常运行。

#### Java Web Spring Boot 测试
启动服务，出现如下内容：
```log
2025-12-11T09:46:54.782+08:00  INFO 29036 --- [           main] com.example.demo.config.EsIndexConfig    : ES 索引不存在，将自动创建：my_index
2025-12-11T09:46:54.970+08:00  INFO 29036 --- [           main] com.example.demo.config.EsIndexConfig    : ES 索引创建成功：my_index
2025-12-11T09:46:55.315+08:00  INFO 29036 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-12-11T09:46:55.325+08:00  INFO 29036 --- [           main] com.example.demo.JavaApplication         : Started JavaApplication in 2.749 seconds (process running for 3.419)
```

再次启动服务，出现如下内容：
```log
2025-12-11T10:03:31.492+08:00  INFO 29044 --- [           main] com.example.demo.config.EsIndexConfig    : ES 索引已存在：my_index
2025-12-11T10:03:32.136+08:00  INFO 29044 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-12-11T10:03:32.148+08:00  INFO 29044 --- [           main] com.example.demo.JavaApplication         : Started JavaApplication in 6.296 seconds (process running for 7.882)
```

通过 postman 测试

写入文章  
```bash
POST http://localhost:8080/article/add
Content-Type: application/json
```

Body：
```json
{
"id": "1",
"title": "Elasticsearch 入门教程",
"content": "这是一个关于全⽂搜索的教程。"
}
```


测试搜索  
```bash
GET http://localhost:8080/article/search?keyword=搜索
```

返回示例：

```json
[
  {
    "id": "1",
    "title": "Elasticsearch 入门教程",
    "content": "这是一个关于全⽂搜索的教程。",
    "createdAt": "2025-12-11T10:06:13.8347995"
  }
]
```