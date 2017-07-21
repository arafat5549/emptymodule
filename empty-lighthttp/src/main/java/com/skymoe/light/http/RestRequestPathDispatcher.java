package com.skymoe.light.http;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.exception.RestException;
import com.skymoe.light.http.request.LightHttpRequest;
import com.skymoe.light.http.serial.IObjectSerializer;
import com.skymoe.light.http.util.CollUtil;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.LocalVariableAttribute;

/**
 * 处理Rest风格的请求转发类
 *
 */
public class RestRequestPathDispatcher<T> implements  IRequestPathDispather {

    private static final String EMPTY_STRING = "";

    private static final String CONTENT_TYPE_CHARSET_PART = " charset=UTF-8";

    // Controller方法元数据，每个元素对应一个http请求
    private Map<String, Method> apiMap;
    // Controller实例，一些单例的集合
    private Map<Class<?>, Object> beanMap;
    // 各Controller方法参数名集合
    private Map<String, String[]> paramNamesMap;
    //spring的context用于注解的扫描(可不用)
    private ApplicationContext context;
    //序列化的类
    private IObjectSerializer serializer;


    public RestRequestPathDispatcher(IObjectSerializer serializer, ApplicationContext context) {
        super();
        this.serializer = serializer;
        this.context =context;

        init();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RestRequestPathDispatcher.class);

    /**
     * 请求转发方法
     * @param request
     * @return
     */
    @Override
    public String dispatch(LightHttpRequest lightrequest,HttpRequest request) {
        String path = lightrequest.getPath();

        if (!"/favicon.ico".equals(path)) { // 过滤静态资源，因为不需要前端页面
            //System.out.println("path="+path);
            //System.out.println("apiMap="+apiMap);
            //System.out.println("beanMap="+beanMap);
            //System.out.println("paramNamesMap="+paramNamesMap);
            Object obj = invoke(request);// 触发controller逻辑
            //System.out.println(obj);
            return this.serializer.serial(obj); //返回JSON字符串
        }
        return null;
    }

    @Override
    public String getContentType() {
        return this.serializer.getMediaType() + CONTENT_TYPE_CHARSET_PART;
    }
    /**
     * 私有类区域
     */
    private void init(){
        //所有带注解的Rest注释 利用spring扫描加载
        //this.pathMappings = new HashMap<String, IRequestHandler<T>>();
        apiMap = new HashMap<>();
        beanMap = new HashMap<>();
        paramNamesMap = new HashMap<>();

        Map<String, Object> allBeans = this.context.getBeansWithAnnotation(Rest.class);
       // LOGGER.info("allBeans="+allBeans);
        for (Object instance : allBeans.values()) {
            // 生成Controller类的实例
            Class<?> clz = instance.getClass();
            beanMap.put(clz, instance);
            // 解析成员方法，也就是各http api
            parseMethods(clz);
        }

    }

    //解析方法
    private void parseMethods(Class<?> clz){
        Rest restClz = clz.getAnnotation(Rest.class);
        //System.out.println("restClz="+restClz.path());

        for (Method method : clz.getDeclaredMethods()) {
            Rest rest = method.getAnnotation(Rest.class);
            // 遍历所有带@Rest注解的方法
            if (rest != null) {
                //获取全路径+
                String wholePath = (restClz!=null ? restClz.path() : "") +  rest.path();
                LOGGER.info("Binding url " +wholePath + " to " + method);
                apiMap.put(wholePath, method);
                // 解析参数名，也就是http请求所带的参数名
                String[] paramNames = parseParamNames(method);
                paramNamesMap.put(wholePath, paramNames);
            }
        }
    }
    //解析参数
    // 这个方法要解析某个方法所有参数的声明名称，这里需要用到javassist的高级反射特性（普通的反射机制是拿不到方法参数声明名称的，只能拿到类型）
    private String[] parseParamNames(Method method){
        try {
            CtMethod cm = ClassPool.getDefault().get(method.getDeclaringClass().getName())
                    .getDeclaredMethod(method.getName());
            LocalVariableAttribute attr = (LocalVariableAttribute) cm.getMethodInfo()
                    .getCodeAttribute().getAttribute(LocalVariableAttribute.tag);

            String[] paramNames = new String[cm.getParameterTypes().length];
            int offset = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;

            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                paramNames[i] = attr.variableName(i + offset);
            }
            return paramNames;
        } catch (NotFoundException e) {
            LOGGER.error("parseParamNames Error", e);
            return new String[] {};
        }
    }
    //



    // 这个就是Handler调用的入口，也就是将HttpRequest映射到对应的方法并映射各参数</span>
    public Object invoke(HttpRequest request) throws RestException {

        String uri = request.getUri();
        int index = uri.indexOf('?');
        // 获取请求路径，映射到Controller的方法
        String path = index >= 0 ? uri.substring(0, index) : uri;

        //LOGGER.info("invoke=="+path);
        Method method = apiMap.get(path);

        if (method == null) {  // 没有注册该方法，直接抛异常
            throw new RestException("No method binded for request " + path);
        }
        try {
            Class<?>[] argClzs = method.getParameterTypes(); // 参数类型
            Object[] args = new Object[argClzs.length]; // 这里放实际的参数值
            String[] paramNames = paramNamesMap.get(path); // 参数名

            // 这个是netty封装的url参数解析工具，当然也可以自己split(",")
            Map<String, List<String>> requestParams = new QueryStringDecoder(uri).parameters();

            // 逐个把请求参数解析成对应方法参数的类型
            for (int i = 0; i < argClzs.length; i++) {
                Class<?> argClz = argClzs[i];
                String paramName = paramNames[i];
                if (!requestParams.containsKey(paramName)
                        || CollUtil.isEmpty(requestParams.get(paramName))  ) {
                    // 没有找到对应参数，则默认取null。愿意的话也可以定义类似@Required或者@DefaultValue之类的注解来设置自定义默认值
                    args[i] = null;
                    continue;
                }

                // 如果带了这个参数，则根据不同的目标类型来解析，先是一些基础类型，这里列的不全，有需要的话可以自己加其他，例如Date
                String param = requestParams.get(paramNames[i]).get(0);
                if (param == null) {
                    args[i] = null;
                } else if (argClz == HttpRequest.class) {
                    args[i] = request;
                } else if (argClz == long.class || argClz == Long.class) {
                    args[i] = Long.valueOf(param);
                } else if (argClz == int.class || argClz == Integer.class) {
                    args[i] = Integer.valueOf(param);
                } else if (argClz == boolean.class || argClz == Boolean.class) {
                    args[i] = Boolean.valueOf(param);
                } else if (argClz == String.class) {
                    args[i] = param;
                } else {
                    // 复合类型的话，默认按照Json方式解析。不过这种场景一般也不需要特别复杂的参数
                    try {
                        args[i] = serializer.fromJson(param,argClz);//JsonMapper.INSTANCE.fromJson(param,argClz);
                    } catch (Exception e) {
                        args[i] = null;
                    }
                }
            }
            // 最后反射调用方法
            Object instance = beanMap.get(method.getDeclaringClass());
            return method.invoke(instance, args);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    public Map<String, Method> getApiMap() {
        return apiMap;
    }
}
