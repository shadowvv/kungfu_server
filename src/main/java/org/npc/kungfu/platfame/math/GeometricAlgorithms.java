package org.npc.kungfu.platfame.math;

import java.util.ArrayList;

public class GeometricAlgorithms {

    /**
     * 检查扇形和矩形碰撞
     */
    public static <T extends Number> boolean isSectorCollidingWithRect(Sector<T> sector, HitBox<T> hitBox) {
        double startAngle = (sector.getStartAngle() + 360) % 360;
        double endAngle = (sector.getEndAngle() + 360) % 360;

        for (VectorTwo<T> point : hitBox.getBoxVectors()) {
            if (isPointInSector(sector, point)) {
                return true;
            }
        }

        double outerRadius = sector.getOuterRadius().doubleValue();
        double innerRadius = sector.getInnerRadius().doubleValue();

        ArrayList<LineSegment<T>> sectorEdges = new ArrayList<>();
        sectorEdges.add(LineSegment.createLineSegment(sector.getCenter(), (VectorTwo<T>) sector.getCenter().addTo(VectorTwo.createDoubleVector(outerRadius * Math.cos(startAngle * Math.PI / 180), outerRadius * Math.sin(startAngle * Math.PI / 180)))));
        sectorEdges.add(LineSegment.createLineSegment(sector.getCenter(), (VectorTwo<T>) sector.getCenter().addTo(VectorTwo.createDoubleVector(outerRadius * Math.cos(endAngle * Math.PI / 180), outerRadius * Math.sin(endAngle * Math.PI / 180)))));

        VectorTwo<T> innerStart = (VectorTwo<T>) sector.getCenter().addTo(VectorTwo.createDoubleVector(innerRadius * Math.cos(startAngle * Math.PI / 180), innerRadius * Math.sin(startAngle * Math.PI / 180)));
        VectorTwo<T> innerEnd = (VectorTwo<T>) sector.getCenter().addTo(VectorTwo.createDoubleVector(innerRadius * Math.cos(endAngle * Math.PI / 180), innerRadius * Math.sin(endAngle * Math.PI / 180)));
        LineSegment<T> innerArcPoints = LineSegment.createLineSegment(innerStart, innerEnd);
        sectorEdges.add(innerArcPoints);

        VectorTwo<T> outerStart = (VectorTwo<T>) sector.getCenter().addTo(VectorTwo.createDoubleVector(outerRadius * Math.cos(startAngle * Math.PI / 180), outerRadius * Math.sin(startAngle * Math.PI / 180)));
        VectorTwo<T> outerEnd = (VectorTwo<T>) sector.getCenter().addTo(VectorTwo.createDoubleVector(outerRadius * Math.cos(endAngle * Math.PI / 180), outerRadius * Math.sin(endAngle * Math.PI / 180)));
        LineSegment<T> outerArcPoints = LineSegment.createLineSegment(outerStart, outerEnd);
        sectorEdges.add(outerArcPoints);

        ArrayList<LineSegment<T>> rectEdges = new ArrayList<>();
        ArrayList<VectorTwo<T>> rectPoints = hitBox.getBoxVectors();
        rectEdges.add(LineSegment.createLineSegment(rectPoints.get(0), rectPoints.get(1)));
        rectEdges.add(LineSegment.createLineSegment(rectPoints.get(1), rectPoints.get(2)));
        rectEdges.add(LineSegment.createLineSegment(rectPoints.get(2), rectPoints.get(3)));
        rectEdges.add(LineSegment.createLineSegment(rectPoints.get(3), rectPoints.get(0)));

        for (LineSegment<T> edge1 : sectorEdges) {
            for (LineSegment<T> edge2 : rectEdges) {
                if (checkLineIntersection(edge1.getStart(), edge1.getEnd(), edge2.getStart(), edge2.getEnd())) {
                    return true;
                }
            }
        }

        return isPointInRect(sector.getCenter(), hitBox);
    }

    /**
     * 判断点是否在扇形范围内
     */
    public static <T extends Number> boolean isPointInSector(Sector<T> sector, VectorTwo<T> point) {
        if (sector.getInnerRadius().doubleValue() > 0 && point.inCirCle(sector.getCenter().getX(), sector.getCenter().getY(), sector.getInnerRadius())) {
            return false;
        }
        if (!point.inCirCle(sector.getCenter().getX(), sector.getCenter().getY(), sector.getOuterRadius())) {
            return false;
        }
        return isAngleWithinRange(point, sector.getCenter(), sector.getStartAngle(), sector.getEndAngle());
    }

    /**
     * 判断点是否在角度范围内
     */
    public static <T extends Number> boolean isAngleWithinRange(VectorTwo<T> point, VectorTwo<T> center, double startAngle, double endAngle) {
        double angle = Math.atan2(point.getY().doubleValue() - center.getY().doubleValue(), point.getX().doubleValue() - center.getX().doubleValue()) * (180 / Math.PI);
        angle = (angle + 360) % 360;
        // 如果扇形跨越 360°，则需要考虑从 startAngle 到 endAngle 可能跨越 0°
        if (startAngle > endAngle) {
            // 角度大于 startAngle 或小于 endAngle 时在扇形区域内
            return (angle >= startAngle || angle <= endAngle);
        } else {
            // 角度在 startAngle 和 endAngle 之间
            return (angle >= startAngle && angle <= endAngle);
        }
    }

    /**
     * 判断线段是否相交
     */
    public static <T extends Number> boolean checkLineIntersection(VectorTwo<T> p1, VectorTwo<T> p2, VectorTwo<T> q1, VectorTwo<T> q2) {
        VectorTwo<T> d1 = p2.subtract(p1);
        VectorTwo<T> d2 = q2.subtract(q1);
        double cross = d1.cross(d2);

        if (cross == 0) {
            return false;
        }

        double t = (q1.subtract(p1)).cross(d2) / cross;
        double u = (q1.subtract(p1)).cross(d1) / cross;

        return t >= 0 && t <= 1 && u >= 0 && u <= 1;
    }

    /**
     * 判断点是否在矩形内部
     */
    public static <T extends Number> boolean isPointInRect(VectorTwo<T> point, HitBox<T> box) {
        ArrayList<VectorTwo<T>> boxVectors = box.getBoxVectors();
        if (boxVectors.size() <= 4) {
            return false;
        }
        VectorTwo<T> topLeft = boxVectors.get(0);
        VectorTwo<T> topRight = boxVectors.get(1);
        VectorTwo<T> bottomRight = boxVectors.get(2);
        VectorTwo<T> bottomLeft = boxVectors.get(3);

        double cross = (topRight.subtract(topLeft)).cross(point.subtract(topLeft));
        if (cross < 0) {
            return false;
        }
        cross = (bottomRight.subtract(topRight)).cross(point.subtract(topRight));
        if (cross < 0) {
            return false;
        }
        cross = (bottomLeft.subtract(bottomRight)).cross(point.subtract(bottomRight));
        if (cross < 0) {
            return false;
        }
        cross = (topLeft.subtract(bottomLeft)).cross(point.subtract(bottomLeft));
        return !(cross < 0);
    }

}
