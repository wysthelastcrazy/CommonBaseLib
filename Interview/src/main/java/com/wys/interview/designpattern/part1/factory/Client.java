package com.wys.interview.designpattern.part1.factory;

import com.wys.interview.designpattern.part1.factory.prodect.Product;
import com.wys.interview.designpattern.part1.factory.simplefactory.SimpleFactory;

/**
 * @author wangyasheng
 * @date 2020/11/3
 * @Describe:
 */
public class Client {
    public void test(){
        SimpleFactory simpleFactory = new SimpleFactory();
        Product product = simpleFactory.createProduct(1);
        //do something with the product
    }
}
