package com.xieyangzhe.spring.demo;

/**
 * @author Yangzhe Xie
 * @date 19/4/20
 */
public class Teacher {
    private String name;
    private Student student;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
