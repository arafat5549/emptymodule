package http;

import com.skymoe.light.http.RestRequestPathDispatcher;
import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.enums.SerialType;
import com.skymoe.light.http.netty.HttpServer;
import com.skymoe.light.http.netty.HttpServerInitializer;
import com.skymoe.light.http.serial.CommonSerializer;
import com.skymoe.light.http.util.scanner.PkgScanner;
import com.skymoe.swagger.data.PetData;
import com.skymoe.swagger.model.Pet;

/**
 * 开启Http服务器
 *
 * 利用了spring Bean扫描功能
 *
 */
public class LightHttpStarter {

    public static void jsonTest() {
        PetData pdata = new PetData();
        CommonSerializer serial = new CommonSerializer();
        Pet pet = pdata.getPetbyId(1);
        String xmlstr = serial.serialize(pet, SerialType.XML);
        System.out.println(xmlstr);

        String str = serial.serialize(pet, SerialType.JSON);
        System.out.println(str);
    }

    /**
     * 用到spring的 包扫描功能#扫描所有带@Rest注解的
     *
     * 开启NettyHttp服务器
     * @param port
     */
    static void startNettyServer(int port){
        //ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:spring/spring-mvc.xml");

        String scanPath = "com.skymoe.web";//"com.skymoe.swagger.resource";
        //"com.skymoe.web"
        PkgScanner ctx = new PkgScanner(scanPath, Rest.class);
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
       // jsonTest();

        startNettyServer(9090);
    }
}
