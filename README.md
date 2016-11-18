# 大创产品规范

## 基本信息
- 名称(暂定): IMhere
- 主要功能: 获取当前的上下文, 并连接到对应服务, 以及实现基于此框架的具体服务。
- 域名(暂定): imhere.zhranklin.com

## 各部分功能详述
### 上下文
上下文信息包括三种形式:

- 基于树莓派无线信号的近距离定位信息
- 扫描的条形码、二维码
- 基于GPS信号的定位信息(可选)

#### 近距离定位信息
目前拟使用wifi信号, 使用WiFi的MAC地址作为识别设备的标志, 在数据库中存储对应的经纬位置、具体位置(例如街道、楼层等信息)。

#### 条形码、二维码
在手机端使用相应的库获取的二维码信息, 包括扫描到的字段以及二维码、条形码的类型(枚举)。

#### GPS位置
GPS位置, 与数据库中存储节点做附近位置匹配, 即对应到相应的数据库条目, 连接到相应服务, 在数据库中存储对应的经纬位置、具体位置。

### 上下文信息提供框架
利用一个专门的服务器处理获取位置信息的请求, 假设其URL为/c, 则对应的请求地址如下:

- `/c/wifi/<mac>` 使用树莓派wifi的mac地址
- `/c/gps/<longtitude>/<latitude>` 使用gps地址
- `/c/code/<type>/<code>` 使用二维码或条形码

当然, 还需要同时进行用户验证(可以使用cookie), 以获取某个用户的个人定制服务。

进行请求后, 会得到一个JSON对象, 格式如下:

```json
{
    "urls": ["http://...", "http://...", "http://..."],
    "token": "..."
}
```

- urls为各种服务组成的URL
- token用来进行第三方服务时使用, 定期更新一次, 与每个用户对应

为了获取token, 也可以在做了用户验证之后直接访问`/c/token`, 返回一个只带"token"的JSON对象:

```json
{
    "token": "..."
}
```

对于对应服务, 如果需要查看某个token对应的是哪个用户, 可以访问`/c/token/<token>`, 返回:

```json
{
    "id": "..."
}
```

得到对应用户的id。

### 服务
根据特定位置能连接到的具体服务, 包括若干种形式:

- 扩展服务的对应http地址, 直接由对应的网页提供服务
- 客户相关的服务, 课程表、日程表等
- 与其他软件相关的某些条目进行关联, 目前包括印象笔记、为知笔记的某条笔记, 用户自定义记录的文字、网页链接等。

对于某个用户来说, 一个上下文可以对应多个服务, 而且除了公共服务外, 其余的是完全可以自己定制的, 用户可以自己选择某个上下文对应哪些服务。不过, 由于扫描条形码、二维码使用方式的原因, 推荐用户只绑定一个服务(最好是笔记)到某个条形码或二维码上。

为了统一服务提供方式, 简化数据库设计, 以及提高扩展性, 将三种服务类型用一种统一的API进行实现。也就是下面所述的具体服务供应框架。

### 具体服务供应
所有的具体服务都是用http实现, 而且使用JSON进行通信。以笔记服务为例: 假设某用户绑定某条笔记对应的url为: /evernote/12345678。

发送的请求时, 使用`/evernote/12345678/<token>`, 具体服务器需要利用位置服务器进行验证, 获取对应id, 然后才能提供对应的业务服务。如果不提供token, 则表示使用public账户(一个虚拟账户, 用来预置各个上下文的对应服务)

请求会返回一个JSON对象, 用来告诉手机执行相关指令:

```json
{
    "public": false,
    "type": "...",
    "content": { 
    }
}
```

- public: 一个boolean, 表示是否为共有的服务(及是否为该上下文的默认服务)
- type: 表示服务类型, 如html, text, redirect, courselist, evernote, wiznote等等
- content: 涉及具体业务逻辑的对象。

在客户端则需要根据cotent对应的对象提交给由type指定的模块进行处理。

### 各个类型的具体服务
#### html/plain
即静态信息, 为html格式, 存储在"text"中, "reminder"则指定该条信息是否需要提醒(例如课表、日程提醒等):

```json
{
    "public": false,
    "type": "html",
    "content": {
        "text": "<p>this is a text. </p>",
        "reminder": false
    }
}
```

#### redirect
重定向, 手机端需要跳转到相应网页。对应的URL存储在"url"中:

```json
{
    "public": true,
    "type": "redirect",
    "content": {
        "url": "http://baidu.com"
    }
}
```

#### evernote/wiznote
TODO: 对应印象笔记和为知笔记两款软件中某条笔记, 需要研究相关API后再规定。

## API使用示例

### 模型
其中Option为可选

```scala
  case class Place(id: String, name: String) extends _idRename
  case class Item(title: String, `type`: String, content: String,
                  place: String, owner: String, id: Option[ObjectId] = None) extends _idRename {
    def withId(id: ObjectId) = Item(title, `type`, content, place, owner, Some(id))
  }
  case class User(username: String, name: String)
  case class UserPass(username: String, name: String, password: String) {
    def asUser = User(username, name)
  }
```

### 示例

```shell
/imh/item GET 用户所有item
# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:18:54]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X GET  localhost:8080/imh/item
[{"title":"title1","type":"html","content":"<h1>test</h1>","place":"001","owner":"zhranklin","id":"582eebcc14ec591dd38b6aa3"},{"title":"title1","type":"html","content":"<h1>test</h1>","place":"001","owner":"zhranklin","id":"582f065f14ec593c0122a661"}]%

/imh/item POST 添加item
# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:40:33]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X POST -d '{"title": "titlexxx", "type":"html", "content": "<h1>test</h1>", "place": "001", "owner": "zhranklin"}' localhost:8080/imh/item
{"title":"titlexxx","type":"html","content":"<h1>test</h1>","place":"001","owner":"zhranklin","id":null}

/imh/item/<id> PUT 修改id为<id>的item
# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:28:41]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X PUT -d '{"title": "title1", "type":"html", "content": "<h1>testtest</h1>", "place": "001", "owner": "zhranklin"}' localhost:8080/imh/item/582eebcc14ec591dd38b6aa3
{"title":"title1","type":"html","content":"<h1>testtest</h1>","place":"001","owner":"zhranklin"}%

/imh/item/<id> GET 获取id为<id>的item
# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:28:53]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X GET localhost:8080/imh/item/582eebcc14ec591dd38b6aa3
{"title":"title1","type":"html","content":"<h1>testtest</h1>","place":"001","owner":"zhranklin","id":"582eebcc14ec591dd38b6aa3"}%

/imh/item/<id> DELETE 删除 同理


/imh/place的道理一样

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:58:33]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X POST -d '{"id": "001", "name": "二基楼"}' localhost:8080/imh/place
{"id":"001","name":"二基楼"}%

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:58:52]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X GET localhost:8080/imh/place/001
{"id":"001","name":"二基楼"}%

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:58:56]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X PUT -d '{"id": "002", "name": "二基楼"}' localhost:8080/imh/place/001
There was an internal server error.%

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [22:59:26]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X PUT -d '{"id": "001", "name": "综合楼"}' localhost:8080/imh/place/001
{"id":"001","name":"综合楼"}%

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [23:00:09]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X GET localhost:8080/imh/place/001                                                                 {"id":"001","name":"综合楼"}%

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [23:00:39]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X DELETE localhost:8080/imh/place/001
{"id":"001","name":"综合楼"}%

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [23:00:53]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X GET localhost:8080/imh/place/001
There was an internal server error.%
(暂时还没做任何异常处理)


user


# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [23:01:53]
→ curl -u zhranklin:pass -H 'Content-Type: application/json' -X GET localhost:8080/imh/user
{"username":"zhranklin","name":"zw"}%

# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [23:06:27]
→ curl -H 'Content-Type: application/json' -X POST -d '{"username": "sht", "password": "pass", "name": "束晗涛"}' localhost:8080/imh/user
{"username":"sht","name":"束晗涛"}


# Zhranklin at sc.10086.cn.defaultbadlist in ~/Dev/notice_crawler on git:master ✖︎ [23:07:45]
→ curl -u sht:pass -H 'Content-Type: application/json' -X GET localhost:8080/imh/user
{"username":"sht","name":"束晗涛"}
```
