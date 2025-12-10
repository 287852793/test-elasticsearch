# test-elasticsearch

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