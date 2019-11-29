# html-container
an static-html-container based on java and nginx  prod: 

### root
nmNLmQtEwGWIt

filename cdci_html/readme.md


#### 什么是CD/CI?
cd current develop 持续开发
ci current integrate 持续部署

#### 为什么要持续开发,持续部署呢?
因为功能都是生长出来的.不可能从一开始就写好,而且就算写好了也要维护,所以需要写一段代码,生产环境就可以看见.

#### 对于static web 来说
使用nginx 和基本的 java web 应该就可以做到

#### 功能描述
每一个操作的单元 叫做一个
    > hash
一个hash 就是一个文件夹
nginx只是类似一个ftp服务器
增删改查 hash
 > hash 列表  hash(hash,name)    // 放弃  随着项目多了 有直接查看所有列表的权限非常不好,权限太高了
 > 增加 是一个hash 库    // 增删改是一个 就是替换  没有就创建  有就替换
 > 没有删除
 > 更改是覆盖
 > 查询使用全文搜索 类似`ctrl shift f`   也是一个hash  // 放弃 原因同上


### 预计工期 
1周  
### 使用技术 
java nginx jenkins(放弃,详情见blog) 

### 框架结构
nginx
    > frp容器
java 
    > 新建容器请求  (new random name dir)
    > 上传请求 

### TODO LIST
[*]  nginx   prod: http://hcen.cc:19000/nmNLmQtEwGWIt/
[ ]  java    test: http://hcen.cc:19001/html-container/api/
[ ]  jenkins // all-my-work-flow
