### maven分模块Module:


**使用到的框架包:**

- netty 用于启动Http服务器
- gson  用于序列化处理
- javaassist 用于反射处理
- logj/slf4j  日记处理包
- common-lang3/guava 通用的工具包

简单说明：

- <!-- 基础模块-代码自动生成和通用工具类（暂时无用）

<module>empty-common</module>-->

- <!-- netty轻量级HTTP服务器 -->

<module>empty-lighthttp</module>

- <!-- web工程 继承了empty-lighthttp模块 -->

<module>empty-web</module>



**基本功能：轻量级的Http入口，以便通过浏览器就能实现便捷的后台功能**

- 1.支持JSON和XML序列化传输,支持多种序列化扩展可配置
- 2.路由功能 类似于Controller
- 3.可传入参数 ?method=xxxx这种方式，可接收传入的参数
- 4.去掉spring的包扫描，自己实现包扫描功能PkgScanner



**实力Demo：**

- 服务器启动Demo在：

empty-web下的 test/java/http/HttpTest.java

- 路由配置Demo在:

empty-web下的 test/java/com/skymoe/web/RestController



- 方法-Demo：

```java
//对应的URL路径:http://localhost:9090/rest/xml?method=111&name=wahaha&child=<user><id>1212</id><name>小孩子</name></user>

@Rest(path = "/xml",method = RequestMethod.GET,serial = SerialType.XML)
public User getUserXML(Integer method,String name,User child){
   System.out.print("method="+method+","+"name="+name+","+"child="+child);
    User user = new User(method,name);
    user.setChild(child);
    //System.out.println(" 访问IP："+ChannelContext.getClientIp());
    return user;
}
```
