package org.npc.kungfu.platfame.math;

public class Sector<T extends Number> {

    private VectorTwo<T> center;
    private T innerRadius;
    private T outerRadius;
    private double startAngle;
    private double endAngle;

    public static Sector<Integer> createIntegerSector(int positionX, int positionY,int innerRadius, int outerRadius,double startAngle,double endAngle) {
        return new Sector<>(VectorTwo.createIntegerVector(positionX,positionY),innerRadius,outerRadius,startAngle,endAngle,Integer.class);
    }

    public static Sector<Double> createDoubleSector(double positionX, double positionY, double innerRadius, double outerRadius,double startAngle,double endAngle) {
        return new Sector<>(VectorTwo.createDoubleVector(positionX, positionY),innerRadius,outerRadius,startAngle,endAngle,Double.class);
    }

    private Sector(VectorTwo<T> center,T innerRadius, T outerRadius, double startAngle, double endAngle,Class<T> type) {
        this.center = center;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    public VectorTwo<T> getCenter() {
        return center;
    }

    public T getInnerRadius() {
        return innerRadius;
    }

    public T getOuterRadius() {
        return outerRadius;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getEndAngle() {
        return endAngle;
    }
}
