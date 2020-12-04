package com.wys.interview.javaBase.reflect;

/**
 * @author wangyasheng
 * @date 2020/11/30
 * @Describe:
 */
public class UserInfo {
    private String name;
    private int age;

    private UserInfo(String name,int age){
        this.name = name;
        this.age = age;
    }
    public void setName(String name){
         this.name = name;
    }
    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
