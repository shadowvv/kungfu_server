package org.npc.kungfu.platfame.math2;

/**
 * 扇形
 */
public class Sector {

    private Vec2 center;//中心点
    private final double innerRadius;//内半径
    private final double outerRadius;//外半径
    private double startAngle;//起始角度
    private double endAngle;//结束角度

    private final double initStartAngle;//初始起始角度
    private final double initEndAngle;//初始结束角度

    /**
     * @param center      中心点
     * @param innerRadius 内半径
     * @param outerRadius 外半径
     * @param startAngle  起始角度
     * @param endAngle    结束角度
     */
    public Sector(Vec2 center, double innerRadius, double outerRadius, double startAngle, double endAngle) {
        this.center = center;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;

        this.initStartAngle = startAngle;
        this.initEndAngle = endAngle;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    /**
     *
     * @return 中心点
     */
    public Vec2 getCenter() {
        return center;
    }

    /**
     *
     * @return 内半径
     */
    public double getInnerRadius() {
        return innerRadius;
    }

    /**
     *
     * @return 外半径
     */
    public double getOuterRadius() {
        return outerRadius;
    }

    /**
     *
     * @return 起始角度
     */
    public double getStartAngle() {
        return startAngle;
    }

    /**
     *
     * @return 结束角度
     */
    public double getEndAngle() {
        return endAngle;
    }

    /**
     * 设置起始角度
     *
     * @param angle 角度
     */
    public void setStartAngle(double angle) {
        startAngle = angle;
    }

    /**
     * 设置结束角度
     *
     * @param angle 角度
     */
    public void setEndAngle(double angle) {
        endAngle = angle;
    }

    /**
     * 设置中心点
     * @param center 中心点
     */
    public void setCenter(Vec2 center) {
        this.center = center;
    }

    /**
     * 更新角度
     *
     * @param angle 角度，用于调整起始角度和结束角度
     */
    public void updateAngle(double angle) {
        // 根据脸部角度更新起始角度
        this.startAngle = this.initStartAngle + angle;
        // 根据脸部角度更新结束角度
        this.endAngle = this.initEndAngle + angle;
    }
}
