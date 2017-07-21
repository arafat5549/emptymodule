package com.skymoe.light.http;

import com.skymoe.light.http.annotation.RequestBean;
import com.skymoe.light.http.request.IRequestHandler;
import com.skymoe.light.http.request.LightHttpRequest;
import com.skymoe.light.http.serial.IObjectSerializer;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 请求分发 暂时不用
 *
 */
@Deprecated
public class SimpleRequestPathDispatcher<T> implements IRequestPathDispather {

	private static final String EMPTY_STRING = "";

	private static final String CONTENT_TYPE_CHARSET_PART = " charset=UTF-8";

	private Map<String, IRequestHandler<T>> pathMappings;

	//@Autowired
	private ApplicationContext context;

	private IObjectSerializer serializer;


	public SimpleRequestPathDispatcher(IObjectSerializer serializer,ApplicationContext context) {
		super();
		this.serializer = serializer;
		this.context =context;

		init();
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRequestPathDispatcher.class);


//	private Map<String, Object> beans;
//	public void setRequestBeans( Map<String, Object> beans){
//		this.beans = beans;
//	}



	//@PostConstruct
	private void init() {
		//System.out.println("==PostConstruct===");

		this.pathMappings = new HashMap<String, IRequestHandler<T>>();
		//FIXME
		Map<String, Object> requestBeans = this.context.getBeansWithAnnotation(RequestBean.class);

		for (Entry<String, Object> entry : requestBeans.entrySet()) {
			Object requestBean = entry.getValue();
			if (!(requestBean instanceof IRequestHandler)) {
				LOGGER.warn("Request bean not instance of IRequestHandler, ignore: " + entry);
			} else {
				@SuppressWarnings("unchecked")
				IRequestHandler<T> requestHandlerBean = (IRequestHandler<T>) requestBean;
				Class<?> requestBeanClazz = AopUtils.getTargetClass(requestBean);
				RequestBean requestBeanAnnotation = requestBeanClazz.getAnnotation(RequestBean.class);
				String path = requestBeanAnnotation.path();
				this.pathMappings.put(path, requestHandlerBean);
				LOGGER.info("Mapping request [" + path + "] to request bean: " + entry);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * IRequestPathDispather#dispatch(com.dangdang.light
	 * .http.LightHttpRequest)
	 */
	@Override
	public String dispatch(LightHttpRequest lightrequest, HttpRequest request) {
		String path = lightrequest.getPath();
		IRequestHandler<T> handler = this.pathMappings.get(path);

		System.out.println("handler="+handler+","+path);
		System.out.println("pathMappings="+pathMappings);
		if (handler != null) {
			return this.serializer.serial(handler.handle(lightrequest));
		}
		return EMPTY_STRING;
	}

	@Override
	public String getContentType() {
		return this.serializer.getMediaType() + CONTENT_TYPE_CHARSET_PART;
	}

}
