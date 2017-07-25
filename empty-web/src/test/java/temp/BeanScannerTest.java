package temp;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.util.scanner.PkgScanner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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


    public static List<URL> getResources(String resourceName, ClassLoader contextClassLoader) {
        try {
            Enumeration<URL> urls = contextClassLoader.getResources(resourceName);
            ArrayList list = new ArrayList(10);

            while(urls.hasMoreElements()) {
                list.add((URL)urls.nextElement());
            }

            return list;
        } catch (IOException var4) {
            return new ArrayList<>();
        }
    }

    static void resScan(){
        List<URL> list = getResources("swagger-ui-master/demo.json", Thread.currentThread().getContextClassLoader());

        if(list.size()>0){
            URL url = list.get(0);
            try {

               String content =  Files.toString(new File(url.getFile()), Charsets.UTF_8);
               System.out.println(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       // System.out.println(list);
    }


    public static void main(String[] args)  {
        resScan();

//        springScanner();
//
//        System.out.println("-----------------------------------");
//
//        try {
//            pkgScanner();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }

    }
}
