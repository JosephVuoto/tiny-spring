package com.xieyangzhe.spring.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xieyangzhe.spring.config.BeanDefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

/**
 * @author Yangzhe Xie
 * @date 18/4/20
 */
public class JsonBeanFactory extends AbstractBeanFactory {
    private String jsonFilePath;

    public JsonBeanFactory(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
        doCreate();
    }

    /**
     * 读传进来的JSON文件并解析
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void doCreate() {
        URL url = Thread.currentThread().getContextClassLoader().getResource(jsonFilePath);
        assert url != null;
        File file = new File(url.getFile());
        byte[] fileContent = new byte[(int) file.length()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
            String json = new String(fileContent);
            Type type = new TypeToken<List<BeanDefinition>>() {
            }.getType();
            List<BeanDefinition> beanDefinitions
                    = new Gson().fromJson(json, type);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                registerBeanDefinition(beanDefinition.getName(), beanDefinition);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
