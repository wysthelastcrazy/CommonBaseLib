package com.wys.interview.designpattern.part1.factory.factoryMethod;

import com.wys.interview.designpattern.part1.factory.prodect.ConcreteProduct1;
import com.wys.interview.designpattern.part1.factory.prodect.Product;

/**
 * @author wangyasheng
 * @date 2020/11/3
 * @Describe:
 */
public class ConcreteFactory1 extends Factory{
    @Override
    public Product factoryMethod() {
        return new ConcreteProduct1();
    }
}
