package com.xieyangzhe.spring.config;

/**
 * @author Yangzhe Xie
 * @date 18/4/20
 */
public class PropertyArg {
    /* 属性名 */
    private String name;
    /* 属性值 */
    private String value;
    /* 属性引用的Bean的类型 */
    private String ref;
    
    /* Getters */
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getRef() {
        return ref;
    }
}
