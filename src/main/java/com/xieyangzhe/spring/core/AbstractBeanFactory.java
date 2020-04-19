package com.xieyangzhe.spring.core;

import com.xieyangzhe.spring.config.BeanDefinition;
import com.xieyangzhe.spring.config.PropertyArg;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yangzhe Xie
 * @date 18/4/20
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    /* 用来保存已经初始化并获取过的Bean实例 */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(32);
    /* 保存Bean的早期引用，用来解决循环依赖 */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /* 保存Bean Definition的容器 */
    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>(32);

    /**
     * 从BeanFactory获取Bean
     * @param name Bean名字
     * @return 获取的Bean
     * @throws Exception Bean初始化时可能抛出的异常
     */
    public Object getBean(String name) throws Exception {
        Object bean = singletonObjects.get(name);
        /* 获取过的直接返回 */
        if (bean != null) {
            return bean;
        }
        Object earlyBean = earlySingletonObjects.get(name);
        /* 有早期引用的也直接返回 */
        if (earlyBean != null) {
            return earlyBean;
        }
        BeanDefinition beanDefinition = beanDefinitions.get(name);
        /* 创建Bean，但并不初始化 */
        bean = createBean(beanDefinition);
        if (bean != null) {
            /* 创建完成后保存一份到早期引用的Map里 */
            earlySingletonObjects.put(name, bean);
            /* 正式初始化各个属性 */
            populateBean(bean, beanDefinition);
            /* 初始化完成后从早期引用的Map删掉并放到已初始化的Map里 */
            singletonObjects.put(name, bean);
            earlySingletonObjects.remove(name);
        }

        return bean;
    }

    /**
     * 将bean definition注册到容器的Map里
     * @param name Bean名字
     * @param bean bean definition
     */
    public void registerBeanDefinition(String name, BeanDefinition bean) {
        beanDefinitions.put(name, bean);
    }

    /**
     * 使用构造函数创建Bean的实例
     * @param beanDefinition bean definition
     * @return Bean的实例
     */
    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        String className = beanDefinition.getClassName();
        Class<?> clazz = loadClass(className);
        if (clazz == null) {
            throw new ClassNotFoundException("Cannot found class: " + className);
        }
        return instanceByCglib(clazz, null, null);
    }

    /**
     * 根据Bean 填充bean的依赖属性
     * @param bean 实际的Bean实例
     * @param beanDefinition bean definition
     */
    private void populateBean(Object bean, BeanDefinition beanDefinition) throws Exception {
        List<PropertyArg> propertyArgList = beanDefinition.getPropertyArgList();
        if (propertyArgList != null) {
            for (PropertyArg propertyArg : propertyArgList) {
                String propertyName = propertyArg.getName();
                String value = propertyArg.getValue();
                String ref = propertyArg.getRef();
                Object resultValue = null;
                if (value != null) {
                    resultValue = value;
                } else if (ref != null && !ref.isEmpty()) {
                    resultValue = getBean(ref);
                }
                if (resultValue != null) {
                    Method method = getPropertySetter(beanDefinition, propertyName, resultValue);
                    method.invoke(bean, resultValue);
                }
            }
        }
    }

    /**
     * 获取Getter方法
     * @param beanDefinition bean definition
     * @param propertyName 属性名
     * @param resultValue 要设置的属性值
     * @return Getter方法
     */
    private Method getPropertySetter(BeanDefinition beanDefinition, String propertyName,
                                     Object resultValue) throws Exception {
        Class<?> beanClass = Class.forName(beanDefinition.getClassName());
        Class<?> injectClazz = resultValue.getClass();
        Class<?> supClass = resultValue.getClass().getSuperclass();
        if (supClass != null && supClass != Object.class) {
            injectClazz = supClass;
        }
        propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return beanClass.getMethod("set" + propertyName, injectClazz);
    }

    private Class<?> loadClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader()
                    .loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object instanceByCglib(Class<?> clz, Constructor<?> ctr, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(NoOp.INSTANCE);

        if (ctr == null) {
            return enhancer.create();
        } else {
            return enhancer.create(ctr.getParameterTypes(), args);
        }
    }

    protected abstract void doCreate();
}
