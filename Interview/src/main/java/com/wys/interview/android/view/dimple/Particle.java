package com.wys.interview.android.view.dimple;

/**
 * @author wangyasheng
 * @date 2020/10/20
 * @Describe:粒子类
 */
class Particle {
    //x 坐标
    public float x;
    //y坐标
    public float y;
    //粒子半径
    public float radius;
    //运动速度
    public float speed;
    //透明度
    public int alpha;

    public Particle(float x,float y,float radius,float speed,int alpha){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.alpha = alpha;
    }
}
