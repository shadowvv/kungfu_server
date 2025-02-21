package org.npc.kungfu.platfame.math2;

/**
 * 二维向量
 */
public class Vec2 {

    private double x;
    private double y;

    /**
     * @param x x轴值
     * @param y y轴值
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return x轴值
     */
    public double getX() {
        return x;
    }

    /**
     *
     * @return y轴值
     */
    public double getY() {
        return y;
    }

    /**
     * 设置x轴
     * @param x 值
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * 设置y轴
     *
     * @param y 值
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * 向量加法
     *
     * @param x x轴值
     * @param y y轴值
     * @return 返回加法结果向量
     */
    public Vec2 add(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }

    /**
     * 向量加法
     * @param other 二维向量
     * @return 返回加法结果向量
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    /**
     * 向量减法
     * @param other 二维向量
     * @return 返回加法结果向量
     */
    public Vec2 subtract(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    /**
     *
     * @return 向量长度
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     *
     * @return 归一化向量
     */
    public Vec2 normalize() {
        double len = length();
        return new Vec2(x / len, y / len);
    }

    /**
     * 计算向量叉积
     * @param other 二维向量
     * @return 向量叉积
     */
    public double cross(Vec2 other) {
        return this.x * other.y - this.y * other.x;
    }

    /**
     * 计算向量点积
     * @param other 二维向量
     * @return 向量点积
     */
    public double dot(Vec2 other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * 判断是否在圆内
     *
     * @param x      x轴值
     * @param y      y轴值
     * @param radius 圆半径
     * @return 是否在圆内
     */
    public boolean inCirCle(double x, double y, double radius) {
        return Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2) <= Math.pow(radius, 2);
    }
}