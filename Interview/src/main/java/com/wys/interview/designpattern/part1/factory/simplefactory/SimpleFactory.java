package com.wys.interview.designpattern.part1.factory.simplefactory;

import com.wys.interview.designpattern.part1.factory.prodect.ConcreteProduct;
import com.wys.interview.designpattern.part1.factory.prodect.ConcreteProduct1;
import com.wys.interview.designpattern.part1.factory.prodect.ConcreteProduct2;
import com.wys.interview.designpattern.part1.factory.prodect.Product;

/**
 * @author wangyasheng
 * @date 2020/11/3
 * @Describe: 简单工厂（Simple Factory）
 * intent：在创建一个对象时不向客户暴露内部细节，并提供一个
 * 创建对象的通用接口。
 *
 * Class Diagram：简单工厂把实例化的操作单独放到一个类中，
 * 这个类就成为简单工厂类，让简单工程类来决定应该用那个具体子类
 * 来实例化。
 * 这样做能把客户类和具体子类的实现解藕，客户类不再需要知道有
 * 哪些子类以及应当实例化那个子类。客户类往往有多个，如果不实用简单
 * 工厂，那么所有的客户类都要知道所有子类的细节。而一旦子类发生
 * 改变，例如增加子类，那么所有的客户类都要进行修改。
 */
public class SimpleFactory {
    public Product createProduct(int type){
        if (type == 1){
            return new ConcreteProduct1();
        }else if (type == 2){
            return new ConcreteProduct2();
        }else {
            return new ConcreteProduct();
        }
    }
}
