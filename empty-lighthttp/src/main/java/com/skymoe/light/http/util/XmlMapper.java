package com.skymoe.light.http.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * XML序列化工具
 *
 * 这种序列化需要在要序列化的Bean对象实体上添加@XmlRootElement注解
 */
public class XmlMapper {

    public static Class<?> unwrapCglib(Object instance) {
        Validate.notNull(instance, "Instance must not be null", new Object[0]);
        Class<?> clazz = instance.getClass();
        if(clazz != null && clazz.getName().contains("$$")) {
            Class<?> superClass = clazz.getSuperclass();
            if(superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }

        return clazz;
    }

    public static RuntimeException unchecked(Throwable t) {
        if(t instanceof RuntimeException) {
            throw (RuntimeException)t;
        } else if(t instanceof Error) {
            throw (Error)t;
        } else {
            throw new RuntimeException(t);
        }
    }
    //

    private static ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap();

    public XmlMapper() {
    }

    public static String toXml(Object root) {
        Class clazz = unwrapCglib(root);
        return toXml((Object)root, (Class)clazz, (String)null);
    }

    public static String toXml(Object root, String encoding) {
        Class clazz = unwrapCglib(root);
        return toXml(root, clazz, encoding);
    }

    public static String toXml(Object root, Class clazz, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException var4) {
            throw unchecked(var4);
        }
    }

    public static String toXml(Collection<?> root, String rootName, Class clazz) {
        return toXml(root, rootName, clazz, (String)null);
    }

    public static String toXml(Collection<?> root, String rootName, Class clazz, String encoding) {
        try {
            XmlMapper.CollectionWrapper wrapper = new XmlMapper.CollectionWrapper();
            wrapper.collection = root;
            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement(new QName(rootName), XmlMapper.CollectionWrapper.class, wrapper);
            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(wrapperElement, writer);
            return writer.toString();
        } catch (JAXBException var7) {
            throw unchecked(var7);
        }
    }

    public static <T> T fromXml(String xml, Class<T> clazz) {
        try {
            StringReader reader = new StringReader(xml);
            return (T)createUnmarshaller(clazz).unmarshal(reader);
        } catch (JAXBException var3) {
            throw unchecked(var3);
        }
    }

    public static Marshaller createMarshaller(Class clazz, String encoding) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            if(StringUtils.isNotBlank(encoding)) {
                marshaller.setProperty("jaxb.encoding", encoding);
            }

            return marshaller;
        } catch (JAXBException var4) {
            throw unchecked(var4);
        }
    }

    public static Unmarshaller createUnmarshaller(Class clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException var2) {
            throw unchecked(var2);
        }
    }

    protected static JAXBContext getJaxbContext(Class clazz) {
        Validate.notNull(clazz, "'clazz' must not be null", new Object[0]);
        JAXBContext jaxbContext = (JAXBContext)jaxbContexts.get(clazz);
        if(jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(new Class[]{clazz, XmlMapper.CollectionWrapper.class});
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            } catch (JAXBException var3) {
                throw new RuntimeException("Could not instantiate JAXBContext for class [" + clazz + "]: " + var3.getMessage(), var3);
            }
        }

        return jaxbContext;
    }

    public static class CollectionWrapper {
        @XmlAnyElement
        protected Collection<?> collection;

        public CollectionWrapper() {
        }
    }

}
