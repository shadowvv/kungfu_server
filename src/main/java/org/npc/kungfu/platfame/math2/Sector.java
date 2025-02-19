package org.npc.kungfu.platfame.math2;

public class Sector {

    private Vec2 center;
    private final double innerRadius;
    private final double outerRadius;
    private double startAngle;
    private double endAngle;
    private final double initStartAngle;
    private final double initEndAngle;

    public Sector(Vec2 center, double innerRadius, double outerRadius, double startAngle, double endAngle) {
        this.center = center;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;

        this.initStartAngle = startAngle;
        this.initEndAngle = endAngle;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    public Vec2 getCenter() {
        return center;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getEndAngle() {
        return endAngle;
    }

    public void setStartAngle(double v) {
        startAngle = v;
    }

    public void setEndAngle(double v) {
        endAngle = v;
    }

    public void setCenter(Vec2 center) {
        this.center = center;
    }

    public void updateAngle(int faceAngle) {
        this.startAngle = this.initStartAngle + faceAngle;
        this.endAngle = this.initEndAngle + faceAngle;
    }
}
