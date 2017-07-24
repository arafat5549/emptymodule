package temp;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.util.scanner.PkgScanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */
public class BeanScannerTest {

     static void springScanner(){
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:spring/spring-mvc.xml");
//
//         Map<String, Object> allBeans = ctx.getBeansWithAnnotation(Rest.class);
//         for (String key :allBeans.keySet() ) {
//             System.out.println(key+","+allBeans.get(key));
//         }
    }
    static void pkgScanner() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        PkgScanner scanner = new PkgScanner("com.skymoe.web", Rest.class);
        List<String> list = scanner.scan();

        Map<String,Object> beans = new HashMap<>();
        for (String name: list) {
            //System.out.println(name);
            Object obj =  Class.forName(name).newInstance();

            beans.put(name,obj);

           // System.out.println(obj);
        }

       // System.out.println(list);
        for (String key :beans.keySet() ) {
            System.out.println(key+","+beans.get(key));
        }
    }


    public static void main(String[] args)  {
       // BeanScannerUtil.parseBeans("com.skymoe.web");

        springScanner();

        System.out.println("-----------------------------------");

        try {
            pkgScanner();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
