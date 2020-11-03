package com.wys.interview.designpattern.part1.factory.factoryMethod;

import com.wys.interview.designpattern.part1.factory.prodect.Product;

/**
 * @author wangyasheng
 * @date 2020/11/3
 * @Describe: 工厂方法（Factory Method）
 *
 * Intent：定义了一个创建对象的接口，但由子类决定要实例化
 * 那个类。工厂方法把实例化操作推迟到子类。
 *
 * Class Diagram：在简单工厂种，创建对象的是另一个类，
 * 而在工厂方法种，是由子类来创建对象。
 */
public abstract class Factory {
    public abstract Product factoryMethod();
    public void doSomething(){
        Product product = factoryMethod();
        //do something with the product
    }
}
