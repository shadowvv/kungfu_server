package org.npc.kungfu.platfame.math;

import java.util.ArrayList;

public class HitBox<T extends Number> {

    private VectorTwo<T> leftTop;
    private T width;
    private T height;
    private GenericMath.GenericMathInner<T> genericMath;

    public static HitBox<Integer> createIntegerHitBox(int left, int top,int width, int height) {
        return new HitBox<>(VectorTwo.createIntegerVector(left,top),width,height,Integer.class);
    }

    public static HitBox<Double> createDoubleHitBox(double left, double top, double width, double height) {
        return new HitBox<>(VectorTwo.createDoubleVector(left, top),width,height, Double.class);
    }

    private HitBox(VectorTwo<T> leftTop, T width, T height,Class<T> type) {
        this.leftTop = leftTop;
        this.width = width;
        this.height = height;
        this.genericMath = GenericMath.getGenericMathByType(type); // 通过类型获取泛型运算
    }

    public ArrayList<VectorTwo<T>> getBoxVectors() {
        ArrayList<VectorTwo<T>> boxVectors = new ArrayList<>();
        boxVectors.add(leftTop);
        boxVectors.add(VectorTwo.createVector(genericMath.add(leftTop.getX(),width),this.leftTop.getY(),genericMath));
        boxVectors.add(VectorTwo.createVector(genericMath.add(leftTop.getX(),width),genericMath.add(this.leftTop.getY(),height),genericMath));
        boxVectors.add(VectorTwo.createVector(leftTop.getX(),genericMath.add(this.leftTop.getY(),height),genericMath));
        return boxVectors;
    }

}
