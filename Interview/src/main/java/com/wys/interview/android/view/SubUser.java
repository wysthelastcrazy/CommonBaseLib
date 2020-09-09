package com.wys.interview.android.view;


import com.wys.interview.android.User;

/**
 * @author wangyasheng
 * @date 2020/8/31
 * @Describe:
 */
class SubUser extends User {
    public SubUser(String name, int age) {
        super(name, age);
    }

    private void test(){
        Sample sample = new Sample();
        String s = sample.str;
    }
    class Sample{
        private String str = "str";
    }
}
