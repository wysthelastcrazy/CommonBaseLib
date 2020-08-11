package com.wys.interview.android.view.util;

import com.wys.interview.android.view.cannon.Intervals;

public class AngleCalculator {


    private final float baseX, baseY;


    public AngleCalculator(float baseX, float baseY) {
        this.baseX = baseX;
        this.baseY = baseY;
    }


    public float calculateAngleWithBasePoint(float x1, float y1) {

        //turn up
        float x = x1 - baseX;
        float y = baseY - y1;

        float hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        float cos = x / hypotenuse;

        float radian = (float) Math.acos(cos);

        float angle = (float) (180 / (Math.PI / radian));

        if (y < 0) {
            angle = -angle;
        } else if (y == 0 && x < 0) {
            angle = 180;
        }

        return angle;
    }

    public float calculateAngle(float x1, float y1, float x2, float y2) {

        float a1 = turnS2G_W((int) x1);
        float a2 = turnS2G_W((int) x2);
        float b1 = turnS2G_H((int) y1);
        float b2 = turnS2G_H((int) y2);

        float key = getK(a2 - a1, b2 - b1);


        return (float) Math.atan(key);


//        float x = x2 - x1;
//        float y = y2 - y1;
//
//        float hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
//
//        float cos = x / hypotenuse;
//
//        float radian = (float) Math.acos(cos);
//
//        float angle = (float) (180 / (Math.PI / radian));
//
//        if (y < 0) {
//            angle = -angle;
//        } else if (y == 0 && x < 0) {
//            angle = 180;
//        }
//
//        return angle;
    }

    public int calcTopX(float x2, float y2) {
        return calcTopXWithY(0, x2, y2);
    }

    public int calcTopXWithY(float top, float x2, float y2) {
        return (int) (baseX + (top - baseY) * (x2 - baseX) / (y2 - baseY));
    }

    public int calcYwithX(float x, float a, float b) {
        return (int) ((x - baseX) * (a - baseX) / (b - baseY) + baseY);
    }

    public int caleXWithY(float y, float a, float b) {
        return (int) ((y - baseY) * (b - baseY) / (a - baseX) + baseX);
    }

    public static float calculateLineLength(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;

        float hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return hypotenuse;
    }


    public int catchIndex(final float angleOption, final float maxAngle, final float minAngle, final int options) {

        float angleOffset = (maxAngle - minAngle) / options;

        int index = options - 1;
        float angle = minAngle;

        /**
         *只有一个选项 直接返回0；
         */
        if (index < 1) {
            return 0;
        }

        /**
         * 小于最小角度 返回最后一个index
         */
        if (angleOption < minAngle) {
            return options - 1;
        }

        while (angle <= maxAngle && index > 0) {

            if (angleOption <= angle + angleOffset) {
                return index;
            }

            index--;
            angle += angleOffset;
        }
        return 0;

    }


    private Intervals turnS2G_I(Intervals intervals) {
        Intervals i = new Intervals(turnS2G_W(intervals.getLeft()),
                turnS2G_W(intervals.getRight()),
                turnS2G_H(intervals.getTop()),
                turnS2G_H(intervals.getBottom()));
//        i.left = turnS2G_W(intervals.getLeft());
//        i.right = turnS2G_W(intervals.getRight());
//        i.top = turnS2G_H(intervals.getTop());
//        i.bottom = turnS2G_H(intervals.getBottom());
        return i;
    }

//    private boolean isInInterval(float y, float key, Intervals intervals) {
//        float x = y / key;
//        return x >= intervals.left && x <= intervals.right;
//    }


    public boolean isInInterval(float touchX, float touchY, Intervals intervals) {
        Intervals i = turnS2G_I(intervals);
        touchX = turnS2G_W((int) touchX);
        touchY = turnS2G_H((int) touchY);
        float k = getK(touchX, touchY);

        float yl = k * i.getLeft();
        float yr = k * i.getRight();
        float xt = i.getTop() * 1.0f / k;
        float xb = i.getBottom() * 1.0f / k;

        return (yl >= i.getBottom() && yl <= i.getTop()) ||
                (yr >= i.getBottom() && yr <= i.getTop()) ||
                (xt >= i.getLeft() && xt <= i.getRight()) ||
                (xb >= i.getLeft() && xb <= i.getRight());


//        value = turnG2S_H((int) value);
//        float x = value / key;
//        float y = x * key;
//        Log.e("calc", String.format("x is %f, left is %d, right is %d", x, i.left, i.right));
//        return x >= i.left && x <= i.right;
    }


    private int turnS2G_W(int x) {
        return (int) (x - baseX);
    }

    private int turnG2S_W(int x) {
        return (int) (baseX + x);
    }

    private int turnS2G_H(int y) {
        return (int) (baseY - y);
    }

    private int turnG2S_H(int y) {
        return (int) (baseY - y);
    }


    private float getK(float x, float y) {
        return y / x;
    }


    public int getXPointOnScreenWidthY(int y, float touchX, float touchY) {
        y = turnS2G_H(y);
        touchX = turnS2G_W((int) touchX);
        touchY = turnS2G_H((int) touchY);
        float key = getK(touchX, touchY);
        int x = (int) (y / key);
        return turnG2S_W(x);
    }

    public int getYPointOnScreenWithX(int x, float touchX, float touchY) {
        x = turnS2G_W(x);
        touchX = turnS2G_W((int) touchX);
        touchY = turnS2G_H((int) touchY);
        float key = getK(touchX, touchY);
        int y = (int) (x * key);
        return turnG2S_H(y);
    }


    public int getXWithAngle(float angle, float y) {
        y = turnS2G_H((int) y);
        float x = (float) (y / Math.tan(turn2Radians(angle)));
//        Log.e("calc", String.format("angle is %f , tan(angle) is %f , x is %f", angle, (float) Math.tan(turn2Radians(angle)), x));
        return turnG2S_W((int) x);
    }

    private float turn2Radians(float a) {
        return (float) (a * Math.PI / 180);
    }

}
