package com.xieyangzhe.spring.config;

import java.util.List;

/**
 * @author Yangzhe Xie
 * @date 18/4/20
 */
public class BeanDefinition {
    private String name;

    /* 类的全限定名 */
    private String className;

    /* 设置的属性 */
    private List<PropertyArg> propertyArgList;

    /* Getters */
    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public List<PropertyArg> getPropertyArgList() {
        return propertyArgList;
    }
}
