package org.npc.kungfu.platfame.math;

public class VectorTwo<T extends Number> {

    private T x;
    private T y;
    private final GenericMath.GenericMathInner<T> genericMath;

    public static <T extends Number> VectorTwo<T> createVector(T x, T y, GenericMath.GenericMathInner<T> genericMath) {
        return new VectorTwo<T>(x, y, genericMath);
    }

    public static VectorTwo<Integer> createIntegerVector(int x, int y) {
        return new VectorTwo<>(x, y, Integer.class);
    }

    public static VectorTwo<Double> createDoubleVector(double x, double y) {
        return new VectorTwo<>(x, y, Double.class);
    }

    private VectorTwo(T x, T y, GenericMath.GenericMathInner<T> genericMath) {
        this.x = x;
        this.y = y;
        this.genericMath = genericMath;
    }

    private VectorTwo(final T x, final T y, Class<T> type) {
        this.x = x;
        this.y = y;
        this.genericMath = GenericMath.getGenericMathByType(type);
    }

    public T getX() {
        return x;
    }

    public void setX(final T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(final T y) {
        this.y = y;
    }

    public void moveTo(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public boolean inCirCle(T x, T y, T circleRadius) {
        T x_dis = genericMath.divide(this.x, x);
        T y_dis = genericMath.divide(this.y, y);

        T x_dis_pow = genericMath.multiply(x_dis, x_dis);
        T y_dis_pow = genericMath.multiply(y_dis, y_dis);
        T dis_pow = genericMath.multiply(circleRadius, circleRadius);

        T xy_dis_pow = genericMath.add(x_dis_pow, y_dis_pow);
        return genericMath.biggerThen(dis_pow, xy_dis_pow);
    }

    public VectorTwo<T> subtract(VectorTwo<T> other) {
        T newX = genericMath.subtract(this.x, other.x);
        T newY = genericMath.subtract(this.y, other.y);
        return new VectorTwo<>(newX, newY, genericMath);
    }

    public double cross(VectorTwo<T> other) {
        T num1 = genericMath.multiply(this.x, other.y);
        T num2 = genericMath.multiply(other.x, this.y);
        return genericMath.divide(num1, num2).doubleValue();
    }

    public VectorTwo<Double> addTo(VectorTwo<Double> doubleVector) {
        return createDoubleVector(this.x.doubleValue() + doubleVector.x, this.y.doubleValue() + doubleVector.y);
    }
}
