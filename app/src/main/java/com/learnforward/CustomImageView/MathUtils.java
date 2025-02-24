package com.learnforward.CustomImageView;

import android.graphics.PointF;
import android.view.MotionEvent;

public class MathUtils {

    public static double distance(MotionEvent event) {
        double x = event.getX(0) - event.getX(1);
        double y = event.getY(0) - event.getY(1);
        return Math.sqrt(x * x + y * y);
    }

    public static double distance(PointF p1, PointF p2) {
        double x = p1.x - p2.x;
        double y = p1.y - p2.y;
        return Math.sqrt(x * x + y * y);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        return Math.sqrt(x * x + y * y);
    }

    public static void midpoint(MotionEvent event, PointF point) {
        float x1 = event.getX(0);
        float y1 = event.getY(0);
        float x2 = event.getX(1);
        float y2 = event.getY(1);
        midpoint(x1, y1, x2, y2, point);
    }

    public static void midpoint(float x1, float  y1, float  x2, float y2, PointF point) {
        point.x = (x1 + x2) / 2.0f;
        point.y = (y1 + y2) / 2.0f;
    }
    /**
     * Rotates p1 around p2 by angle degrees.
     * @param p1
     * @param p2
     * @param angle
     */
    public void rotate(PointF p1, PointF p2, float  angle) {
        float  px = p1.x;
        float  py = p1.y;
        float  ox = p2.x;
        float  oy = p2.y;
        p1.x = (float) (Math.cos(angle) * (px-ox) - Math.sin(angle) * (py-oy) + ox);
        p1.y = (float) (Math.sin(angle) * (px-ox) + Math.cos(angle) * (py-oy) + oy);
    }

    public static double angle(PointF p1, PointF p2) {
        return angle(p1.x, p1.y, p2.x, p2.y);
    }

    public static double angle(double x1, double y1, double x2, double y2) {
        return (double) Math.atan2(y2 - y1, x2 - x1);
    }
}
