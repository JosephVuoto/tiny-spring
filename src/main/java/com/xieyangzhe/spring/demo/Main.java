package com.xieyangzhe.spring.demo;

import com.xieyangzhe.spring.core.JsonBeanFactory;

/**
 * @author Yangzhe Xie
 * @date 18/4/20
 */
public class Main {
    public static void main(String[] args) throws Exception {
        JsonBeanFactory jsonBeanFactory = new JsonBeanFactory("application.json");
        Student student = (Student) jsonBeanFactory.getBean("student");
        Teacher teacher = (Teacher) jsonBeanFactory.getBean("teacher");
        System.out.println(student.getTeacher().getName());
        System.out.println(teacher.getStudent().getName());
    }
}
