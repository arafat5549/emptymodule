package http;

import com.skymoe.light.http.RestRequestPathDispatcher;
import com.skymoe.light.http.enums.SerialType;
import com.skymoe.light.http.netty.HttpServer;
import com.skymoe.light.http.netty.HttpServerInitializer;
import com.skymoe.light.http.serial.CommonSerializer;
import com.skymoe.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 开启Http服务器
 *
 * 利用了spring Bean扫描功能
 *
 */
public class HttpTest {

    public static void jsonTest() {
        CommonSerializer serial = new CommonSerializer();
        User user = new User(1,"wang");
        String str = serial.serialize(user, SerialType.XML);
        System.out.println(str);
    }

    /**
     * 用到spring的 包扫描功能#扫描所有带@Rest注解的
     *
     * 开启NettyHttp服务器
     * @param port
     */
    static void startNettyServer(int port){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:spring/spring-mvc.xml");
        CommonSerializer serializer = new CommonSerializer();
        RestRequestPathDispatcher dispatcher = new RestRequestPathDispatcher(serializer,ctx);
        HttpServerInitializer init = new HttpServerInitializer(1,dispatcher);
        HttpServer server = new HttpServer(init,port);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //jsonTest();

        startNettyServer(9090);
    }
}
