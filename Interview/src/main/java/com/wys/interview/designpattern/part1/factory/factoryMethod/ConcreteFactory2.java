package com.wys.interview.designpattern.part1.factory.factoryMethod;

import com.wys.interview.designpattern.part1.factory.prodect.ConcreteProduct2;
import com.wys.interview.designpattern.part1.factory.prodect.Product;

/**
 * @author wangyasheng
 * @date 2020/11/3
 * @Describe:
 */
public class ConcreteFactory2 extends Factory{
    @Override
    public Product factoryMethod() {
        return new ConcreteProduct2();
    }
}
