package org.npc.kungfu.platfame.math2;

import java.util.List;

/**
 * 碰撞检测工具类
 */
public class CollisionUtils {

    /**
     * 检测扇形与矩形是否碰撞
     * 该方法用于判断一个扇形区域是否与一个矩形区域发生碰撞或重叠
     * 主要通过检查矩形的顶点是否在扇形内、扇形的边缘是否与矩形的边缘相交，以及扇形中心是否在矩形内来确定
     *
     * @param attackSector 扇形区域对象，包含扇形的中心点、起始角度、结束角度和外半径
     * @param rectVertices 矩形的四个顶点，按顺序提供
     * @return 如果扇形与矩形发生碰撞或重叠，则返回true；否则返回false
     */
    public static boolean isSectorCollidingWithRect(Sector attackSector, List<Vec2> rectVertices) {
        // 输入参数检查
        if (attackSector == null || rectVertices == null || rectVertices.size() != 4) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        // 确保角度在 0-360 之间
        double startAngle = normalizeAngle(attackSector.getStartAngle());
        double endAngle = normalizeAngle(attackSector.getEndAngle());

        // 检查矩形顶点是否在扇形内
        for (Vec2 vertex : rectVertices) {
            if (isPointInSector(vertex, attackSector)) {
                return true;
            }
        }

        // 扇形边缘线段
        Vec2 center = attackSector.getCenter();
        double outerRadius = attackSector.getOuterRadius();
        List<Vec2[]> sectorEdges = List.of(
                new Vec2[]{center, center.add(new Vec2(outerRadius * Math.cos(Math.toRadians(startAngle)), outerRadius * Math.sin(Math.toRadians(startAngle))))},
                new Vec2[]{center, center.add(new Vec2(outerRadius * Math.cos(Math.toRadians(endAngle)), outerRadius * Math.sin(Math.toRadians(endAngle))))}
        );

        // 矩形边缘线段
        List<Vec2[]> rectEdges = List.of(
                new Vec2[]{rectVertices.get(0), rectVertices.get(1)},
                new Vec2[]{rectVertices.get(1), rectVertices.get(2)},
                new Vec2[]{rectVertices.get(2), rectVertices.get(3)},
                new Vec2[]{rectVertices.get(3), rectVertices.get(0)}
        );

        // 检测扇形边缘与矩形边缘是否相交
        for (Vec2[] sectorEdge : sectorEdges) {
            for (Vec2[] rectEdge : rectEdges) {
                if (checkLineIntersection(sectorEdge[0], sectorEdge[1], rectEdge[0], rectEdge[1], attackSector.getCenter(), attackSector.getInnerRadius())) {
                    return true;
                }
            }
        }

        // 检测扇形中心是否在矩形内
        return isPointInRect(center, rectVertices);
    }

    /**
     * 正则化角度，将角度限制在 0-360 之间
     *
     * @param angle 需要正则化的角度
     * @return 正则化后的角度
     */
    private static double normalizeAngle(double angle) {
        return (angle % 360 + 360) % 360;
    }

    /**
     * 判断点是否在扇形内
     * @param point 需要判断的点
     * @param attackSector 扇形对象
     * @return 是否在扇形内
     */
    private static boolean isPointInSector(Vec2 point, Sector attackSector) {
        if (point == null || attackSector == null || attackSector.getCenter() == null) {
            throw new IllegalArgumentException("Point or sector cannot be null");
        }

        double innerRadius = attackSector.getInnerRadius();
        double outerRadius = attackSector.getOuterRadius();

        if (innerRadius > outerRadius) {
            throw new IllegalArgumentException("Inner radius cannot be greater than outer radius");
        }

        Vec2 center = attackSector.getCenter();
        double dx = point.getX() - center.getX();
        double dy = point.getY() - center.getY();
        double distanceSquared = dx * dx + dy * dy;

        if (distanceSquared < innerRadius * innerRadius || distanceSquared > outerRadius * outerRadius) {
            return false;
        }

        return isAngleWithinRange(point, center, attackSector.getStartAngle(), attackSector.getEndAngle());
    }

    /**
     * 判断点是否在指定角度范围内
     *
     * @param point      需要判断的点
     * @param center     扇形中心点
     * @param startAngle 扇形起始角度
     * @param endAngle   扇形结束角度
     * @return 是否在指定角度范围内
     */
    private static boolean isAngleWithinRange(Vec2 point, Vec2 center, double startAngle, double endAngle) {
        if (point == null || center == null) {
            throw new IllegalArgumentException("Point or center cannot be null");
        }

        // 处理角度跨越 360 度的情况
        double angle = Math.toDegrees(Math.atan2(point.getY() - center.getY(), point.getX() - center.getX()));
        if (angle < 0) {
            angle += 360;
        }

        if (startAngle <= endAngle) {
            return angle >= startAngle && angle <= endAngle;
        } else {
            return angle >= startAngle || angle <= endAngle;
        }
    }

    /**
     * 检查两条线段是否相交
     * 该方法主要用于判断两条线段是否在指定的扇区内相交，排除内圆内的交点
     *
     * @param p1 线段1的起点
     * @param p2 线段1的终点
     * @param q1 线段2的起点
     * @param q2 线段2的终点
     * @param sectorCenter 扇区的中心点
     * @param innerRadius 扇区的内半径
     * @return 如果线段相交且不在内圆内，则返回true，否则返回false
     */
    private static boolean checkLineIntersection(
            Vec2 p1,
            Vec2 p2,
            Vec2 q1,
            Vec2 q2,
            Vec2 sectorCenter,
            double innerRadius
    ) {
        // Step 0: 空指针检查
        if (p1 == null || p2 == null || q1 == null || q2 == null || sectorCenter == null) {
            throw new IllegalArgumentException("Input points or sector center cannot be null");
        }

        // Step 1: 如果任意点在内圆内，直接排除
        double p1Dist = p1.subtract(sectorCenter).length();
        double p2Dist = p2.subtract(sectorCenter).length();
        double q1Dist = q1.subtract(sectorCenter).length();
        double q2Dist = q2.subtract(sectorCenter).length();

        if (p1Dist <= innerRadius || p2Dist <= innerRadius || q1Dist <= innerRadius || q2Dist <= innerRadius) {
            return false;
        }

        // Step 2: 计算线段向量
        Vec2 d1 = new Vec2(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        Vec2 d2 = new Vec2(q2.getX() - q1.getX(), q2.getY() - q1.getY());

        // Step 3: 计算叉积
        double cross = d1.cross(d2);

        // Step 4: 处理浮点数精度误差
        double epsilon = 1e-6;
        if (Math.abs(cross) < epsilon) return false;

        // Step 5: 计算交点参数
        Vec2 q1SubP1 = new Vec2(q1.getX() - p1.getX(), q1.getY() - p1.getY());
        double t = q1SubP1.cross(d2) / cross;
        double u = q1SubP1.cross(d1) / cross;

        // Step 6: 交点是否在线段范围内
        return t >= 0 && t <= 1 && u >= 0 && u <= 1;
    }

    /**
     * 检查点是否在指定的矩形内
     * 假设矩形是轴对齐的（AABB，Axis-Aligned Bounding Box）
     *
     * @param point        要检查的点
     * @param rectVertices 矩形的四个顶点，应按顺时针或逆时针顺序提供
     * @return 如果点在矩形内，则返回true；否则返回false
     * @throws IllegalArgumentException 如果rectVertices不包含四个顶点
     */
    private static boolean isPointInRect(Vec2 point, List<Vec2> rectVertices) {
        // 输入验证
        if (rectVertices == null || rectVertices.size() != 4) {
            throw new IllegalArgumentException("rectVertices 必须包含四个顶点");
        }

        // 获取矩形的四个顶点
        Vec2 topLeft = rectVertices.get(0);
        Vec2 topRight = rectVertices.get(1);
        Vec2 bottomRight = rectVertices.get(2);
        Vec2 bottomLeft = rectVertices.get(3);

        // 计算矩形的边界（最小和最大x、y值）
        double minX = Math.min(topLeft.getX(), bottomLeft.getX());
        double maxX = Math.max(topRight.getX(), bottomRight.getX());
        double minY = Math.min(topLeft.getY(), topRight.getY());
        double maxY = Math.max(bottomLeft.getY(), bottomRight.getY());

        // 检查点是否在矩形的边界内
        return point.getX() >= minX && point.getX() <= maxX && point.getY() >= minY && point.getY() <= maxY;
    }

}
