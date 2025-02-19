package org.npc.kungfu.platfame.math2;

import java.util.List;

public class CollisionUtils {

    // 检测扇形与矩形是否碰撞
    public static boolean isSectorCollidingWithRect(
            Sector attackSector,
            List<Vec2> rectVertices
    ) {
        // 处理角度范围，确保在 0-360 之间
        attackSector.setStartAngle((attackSector.getStartAngle() % 360 + 360) % 360);
        attackSector.setEndAngle((attackSector.getEndAngle() % 360 + 360) % 360);

        // 检查矩形顶点是否在扇形内
        for (Vec2 vertex : rectVertices) {
            if (isPointInSector(vertex, attackSector)) {
                return true;
            }
        }

        // 扇形边缘线段
        List<Vec2[]> sectorEdges = List.of(
                new Vec2[]{attackSector.getCenter(), attackSector.getCenter().add(new Vec2(
                        attackSector.getOuterRadius() * Math.cos(Math.toRadians(attackSector.getStartAngle())),
                        attackSector.getOuterRadius() * Math.sin(Math.toRadians(attackSector.getStartAngle()))
                ))},
                new Vec2[]{attackSector.getCenter(), attackSector.getCenter().add(new Vec2(
                        attackSector.getOuterRadius() * Math.cos(Math.toRadians(attackSector.getEndAngle())),
                        attackSector.getOuterRadius() * Math.sin(Math.toRadians(attackSector.getEndAngle()))
                ))}
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
        return isPointInRect(attackSector.getCenter(), rectVertices);
    }

    // 判断点是否在扇形内
    private static boolean isPointInSector(Vec2 point, Sector attackSector) {
        double distance = point.subtract(attackSector.getCenter()).length();
        return distance >= attackSector.getInnerRadius()
                && distance <= attackSector.getOuterRadius()
                && isAngleWithinRange(point, attackSector.getCenter(), attackSector.getStartAngle(), attackSector.getEndAngle());
    }

    // 判断点是否在角度范围内
    private static boolean isAngleWithinRange(
            Vec2 point,
            Vec2 center,
            double startAngle,
            double endAngle
    ) {
        double dx = point.getX() - center.getX();
        double dy = point.getY() - center.getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        angle = (angle + 360) % 360;

        if (startAngle <= endAngle) {
            return angle >= startAngle && angle <= endAngle;
        } else {
            // 跨越 0 度的情况
            return angle >= startAngle || angle <= endAngle;
        }
    }

    // 判断两线段是否相交，排除扇形内圆的影响
    private static boolean checkLineIntersection(
            Vec2 p1,
            Vec2 p2,
            Vec2 q1,
            Vec2 q2,
            Vec2 sectorCenter,
            double innerRadius
    ) {
        // Step 1: 如果任意点在内圆内，直接排除
        if (p1.subtract(sectorCenter).length() <= innerRadius ||
                p2.subtract(sectorCenter).length() <= innerRadius ||
                q1.subtract(sectorCenter).length() <= innerRadius ||
                q2.subtract(sectorCenter).length() <= innerRadius) {
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


    // 判断点是否在矩形内
    private static boolean isPointInRect(Vec2 point, List<Vec2> rectVertices) {
        Vec2 topLeft = rectVertices.get(0);
        Vec2 topRight = rectVertices.get(1);
        Vec2 bottomRight = rectVertices.get(2);
        Vec2 bottomLeft = rectVertices.get(3);

        Vec2 edge1 = topRight.subtract(topLeft);
        Vec2 edge2 = bottomRight.subtract(topRight);
        Vec2 edge3 = bottomLeft.subtract(bottomRight);
        Vec2 edge4 = topLeft.subtract(bottomLeft);

        Vec2 v1 = point.subtract(topLeft);
        Vec2 v2 = point.subtract(topRight);
        Vec2 v3 = point.subtract(bottomRight);
        Vec2 v4 = point.subtract(bottomLeft);

        return edge1.cross(v1) >= 0
                && edge2.cross(v2) >= 0
                && edge3.cross(v3) >= 0
                && edge4.cross(v4) >= 0;
    }
}
