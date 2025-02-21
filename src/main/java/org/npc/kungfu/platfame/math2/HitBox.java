package org.npc.kungfu.platfame.math2;

import java.util.ArrayList;

/**
 * 碰撞盒
 */
public class HitBox {

    private Vec2 leftTop;//左上角点
    private final double width;//宽度
    private final double height;//高度

    /**
     * @param leftTop 左上角点
     * @param width   宽度
     * @param height  高度
     */
    public HitBox(Vec2 leftTop, double width, double height) {
        this.leftTop = leftTop;
        this.width = width;
        this.height = height;
    }

    /**
     * 设置左上角点
     *
     * @param x x坐标
     * @param y y坐标
     */
    public void setLeftTop(double x, double y) {
        this.leftTop = new Vec2(x, y);
    }

    /**
     * 获取框的四个顶点向量
     *
     * 此方法用于计算并返回一个框的四个顶点坐标这些顶点是根据框的左上角点、宽度和高度计算的
     * 如果左上角点为null或宽度、高度为负数，则认为输入参数无效，将抛出异常
     *
     * @return 包含四个顶点坐标的列表如果输入参数无效，则返回null
     * @throws IllegalArgumentException 如果左上角点为null或宽度、高度为负数，则抛出此异常
     */
    public ArrayList<Vec2> getBoxVectors() {
        // 检查输入参数的有效性
        if (leftTop == null || width < 0 || height < 0) {
            throw new IllegalArgumentException("Invalid parameters for box vectors");
        }

        // 提取局部变量以减少重复调用
        double x = leftTop.getX();
        double y = leftTop.getY();
        double right = x + width;
        double bottom = y + height;

        // 创建并返回包含四个顶点的列表
        ArrayList<Vec2> boxVectors = new ArrayList<>();
        boxVectors.add(leftTop);
        boxVectors.add(new Vec2(right, y));
        boxVectors.add(new Vec2(right, bottom));
        boxVectors.add(new Vec2(x, bottom));

        return boxVectors;
    }

}
