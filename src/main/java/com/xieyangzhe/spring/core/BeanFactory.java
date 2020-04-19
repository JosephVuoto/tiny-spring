package com.xieyangzhe.spring.core;

import com.xieyangzhe.spring.config.BeanDefinition;

/**
 * @author Yangzhe Xie
 * @date 18/4/20
 */
public interface BeanFactory {
    /**
     * 从BeanFactory获取Bean
     * @param name Bean名字
     * @return 获取的Bean
     * @throws Exception Bean初始化时可能抛出的异常
     */
    Object getBean(String name) throws Exception;

    /**
     * 将bean definition注册到容器
     * @param name Bean名字
     * @param bean bean definition
     */
    void registerBeanDefinition(String name, BeanDefinition bean);
}
