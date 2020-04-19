package com.xieyangzhe.spring.demo;

/**
 * @author Yangzhe Xie
 * @date 18/4/20
 */
public class Student {
    private String name;
    private Teacher teacher;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
