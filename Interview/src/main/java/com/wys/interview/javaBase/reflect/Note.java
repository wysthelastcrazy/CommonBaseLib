package com.wys.interview.javaBase.reflect;

import android.os.Build;
import android.util.Log;

import com.wys.interview.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;

import androidx.annotation.RequiresApi;

/**
 * @author wangyasheng
 * @date 2020/11/30
 * @Describe:反射
 */
class Note {
    /**
     * 反射定义：
     * 1、对于任意一个类，都能知道这个类的所有属性和方法；
     * 2、对于任何一个对象，都能调用它的任意一个属性和方法。
     * 这种动态获取信息以及动态调用对象的方法的功能称为Java语言的反射机制。
     *
     * 反射机制主要涉及两个类：Class和Member
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void test() throws Exception{
        //思维导图
        int res = R.drawable.reflect;
        /**
         * Class
         * Class是反射操作的基础，每个class类，无论创建多少实例对象，
         * 在JVM中都对应同一个Class对象。
         *
         * 在Java反射包java.lang.reflect中所有的类都没有public构造方法，
         * 想要获取这些类的实例，只能通过Class类获取，所以说如果想要使用反射，
         * 必须先获取Class对象。
         *
         * 获取Class对象的几种方法：
         * 1、Object.getClass（）
         *  示例：Class c = "dnd".getClass();
         * 2、The.class
         *  示例：Class c = String.class;
         * 3、Class.forName()
         *  示例：Class c = Class.forName("java.lang.String");
         *       Class cStringArr = Class.forName("[[Ljava.lang.String;");
         * 4、The.TYPE
         *  示例：Class c = Double.TYPE;
         * 5、Class.getSuperclass();
         */
        Class c = UserInfo.class;
        //获取实例 唐桑尼亚
        Constructor constructor = c.getDeclaredConstructor(String.class,int.class);
        constructor.setAccessible(true);
        UserInfo userInfo = (UserInfo) constructor.newInstance("dnd",18);
        Log.d("wys",userInfo.toString());

        /**
         * 通过Class获取类修饰符和类型
         */

        Class cMap = HashMap.class;
        //获取类名
        String className = cMap.getName();
        //获取类限定符 public
        String modifier = Modifier.toString(cMap.getModifiers());
        //获取类泛型参数
        TypeVariable[] typeParameters = cMap.getTypeParameters();
        if (typeParameters.length > 0){
            StringBuilder sb = new StringBuilder("泛型参数：");
            for (TypeVariable typeVariable : typeParameters) {
                sb.append(typeVariable.getName());
                sb.append("___");
            }
            Log.d("wys",sb.toString());
        }else{
            Log.d("wys","泛型参数为空！");
        }

        //获取类所实现的接口
        Type[] interfaces = cMap.getGenericInterfaces();
        if (interfaces.length > 0) {
            StringBuilder sb = new StringBuilder("接口信息：");
            for (Type type : interfaces) {
                sb.append(type.toString());
                sb.append("___");
            }
            Log.d("wys",sb.toString());
        } else {
            Log.d("wys","类接口信息为空！");
        }

        //获取继承的父类信息
        Class superClass = cMap.getSuperclass();

        //获取类注解信息
        Annotation[] annotations = cMap.getAnnotations();
        if (annotations.length != 0) {
            StringBuilder sb = new StringBuilder("类注解信息：");
            for (Annotation annotation : annotations) {
                sb.append(annotation.toString());
                sb.append("___");
            }
            Log.d("wys",sb.toString());
        } else {
            Log.d("wys","类注解信息为空！");
        }


        /**
         * Member
         * Member有三个实现类：
         * 1、Field对应类变量
         * 2、Method对应类方法
         * 3、Constructor对应类的构造方法
         */

        /**
         * Field
         * 通过Field可以访问给定对象的类变量，包括获取变量的类型、
         * 修饰符、注解、变量名、变量值等等，即使变量是private的
         *
         * Class提供了四种方法获得给定类的Field：
         * a、getDeclaredField(String name)获取指定的变量，包括private的
         * b、getField(String name)获取指定变量，只能是public的
         * c、getDeclaredFields()获取所有的变量，包括private的
         * b、getFields()获取所有变量，只能是public的
         */

        Field[] fields = c.getDeclaredFields();
        if (fields.length>0){
            for (Field field: fields){
                StringBuilder sb = new StringBuilder();
                //变量名
                String name = field.getName();
                sb.append("变量名：");
                sb.append(name);

                //变量类型
                String type = field.getType().getName();
                sb.append("变量类型：");
                sb.append(type);
                //变量修饰符
                String modifier1 = Modifier.toString(field.getModifiers());
                sb.append("变量修饰符：");
                sb.append(modifier1);
                //变量上的注解
                Annotation[] annotations1 = field.getDeclaredAnnotations();
                if (annotations1.length > 0) {
                    for (Annotation annotation : annotations) {
                        String annName = annotation.toString();
                        sb.append("注解：");
                        sb.append(annName);
                    }
                }
                Log.d("wys",sb.toString());
            }
        }
        /**
         * 获取、设置变量值
         */
        Log.d("wys","Before: "+userInfo.toString());
        Field nameField = c.getDeclaredField("name");
        //这个方法是 AccessibleObject 中的一个方法，
        // Field、Method、Constructor 都是其子类，
        // 该方法的作用就是可以取消 Java 语言访问权限检查。
        nameField.setAccessible(true);
        Field ageField = c.getDeclaredField("age");
        ageField.setAccessible(true);
        nameField.set(userInfo,"Tom");
        ageField.setInt(userInfo,19);
        Log.d("wys","After: "+userInfo.toString());

        /**
         * Method
         *
         * Class依然提供了四种方法来获取Method：
         * 1、getDeclaredMethod(String name,Class<?>...parameterTypes);
         * 2、getMethod(String name,Class<?>... parameterTypes);
         * 3、getDeclaredMethods();
         * 4、getMethods();
         *
         * 获取带参数方法时，如果参数类型错误会报NoSuchMethodException，对于
         * 参数是泛型的情况下，泛型必须当成Object处理。
         *
         * 获取方法返回值：
         * getReturnType():获取目标方法返回类型对应的Class对象
         * getGenericReturnType():获取目标方法返回类型对应的Type对象
         *
         * 获取方法参数类型：
         * getParameterTypes()：获取目标方法各参数对应的Class对象
         * getGenericParameterTypes()：获取目标方法各参数类型对应的Type对象
         * 以上两个返回值为数组。
         *
         * 获取方法声明抛出的异常类型：
         * getExceptionTypes()：获取目标方法抛出的异常类型对应的Class对象
         * getGenericExceptionTypes()：获取目标方法抛出的异常类型对应的Type对象
         * 以上两个返回值为数组。
         *
         * 获取方法参数名称：
         * .class文件中默认不存储方法参数名称，如果想要获取方法参数名称，需要在
         * 编译的时候加上 -parameters参数。构造方法的参数获取同样。
         *
         * 获取方法修饰符：
         * method.getModifiers();
         *
         * 通过反射调用方法：
         * 凡事通过invoke()方法来调用目标方法。第一个参数为需要调用的目标类对象，
         * 如果方法是static的，则该参数为null，后面的参数都为目标方法的参数值，
         * 顺序与目标方法声明中的参数一致。
         * 需要注意的是，被调用的方法本身所抛出的异常在反射中都会已InvocationTargetException
         * 抛出。换句话说，反射调用过程中如果异常InvocationTargetException抛出，
         * 说明反射调用本身是成功的，因为这个异常是目标方法本身所抛出的异常。
         */
        Method nameMethod = c.getMethod("setName", String.class);
        Parameter[] parameters = nameMethod.getParameters();
        if (parameters.length>0){
            for (Parameter p: parameters){
                StringBuilder sb = new StringBuilder();
                sb.append("参数类型：");
                sb.append(p.getType());
                sb.append("参数名称：");
                //如果编译未加上-parameters参数，返回的参数名称则形如argX，X表示参数位置。
                sb.append(p.getName());
                //输出：参数类型: class java.lang.String参数名称: arg0
                Log.d("wys",sb.toString());
            }
        }

        /**
         * Constructor
         * 通过反射访问构造方法并通过构造方法构建新的对象。
         *
         * Class也提供了四种方法来获取构造方法：
         * 1、getDeclaredConstructor(Class<?>... parameterTypes)
         * 2、getConstructor(Class<?>...parameterTypes)
         * 3、getDeclaredConstructors()
         * 4、getConstructors()
         * 构造方的名称、限定符、参数、声明的异常等获取方法和Method一致。
         *
         * 创建对象：
         * 创建对象有两种方法：
         * 1、constructor.newInstance()
         * 2、Class.newInstance()
         * 优先使用第一种方法，第二种方法已经被废弃。它们之间的区别有：
         * 1、方法一可以调用任意参数的构造方法，而方法二只能调用无参的构造方法；
         * 2、方法一会将原方法抛出的异常都包装成InvocationTargetException抛出，
         *    而方法二会将原方法中的异常不做处理的原样抛出；
         * 3、方法一不需要方法权限，方法二只能调用public的构造方法。
         * 需要注意的是：反射不支持自动装箱，传入参数时要小心。自动装箱实在编译期间的，
         * 而反射是在运行期间。
         */


        /**
         * 反射的优缺点
         *
         * 优点：
         * 动态改变类行为
         *
         * 缺点：
         * 1、性能开销
         *    反射涉及类型动态解析，所以JVM无法对这些代码进行优化。因此，反射操作
         *    效率地下，尽量避免在高性能要求下使用反射。
         * 2、安全限制
         *    使用反射要求程序必须在一个没有安全限制的环境中运行。
         * 3、内部曝光
         *    由于反射允许代码执行一些在正常情况下不被允许的操作，所以使用反射可能会
         *    导致意料之外的副作用。
         */

        HashMap<String,String> map = new HashMap();
        map.put("","");
    }
}
