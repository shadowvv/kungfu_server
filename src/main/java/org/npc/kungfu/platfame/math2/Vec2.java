package org.npc.kungfu.platfame.math2;

public class Vec2 {
    private double x;
    private double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // 向量加法
    public Vec2 add(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }

    // 向量加法
    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    // 向量减法
    public Vec2 subtract(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    // 向量长度
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    // 归一化向量
    public Vec2 normalize() {
        double len = length();
        return new Vec2(x / len, y / len);
    }

    // 向量叉积
    public double cross(Vec2 other) {
        return this.x * other.y - this.y * other.x;
    }

    // 向量点积
    public double dot(Vec2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public boolean inCirCle(int x, int y, float moveRange) {
        return false;
    }
}