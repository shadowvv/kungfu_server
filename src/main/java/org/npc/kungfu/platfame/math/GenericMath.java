package org.npc.kungfu.platfame.math;

import java.util.HashMap;
import java.util.Map;

public class GenericMath {

    private static final Map<Class<?>, GenericMathInner<?>> MATH_OPERATIONS = new HashMap<>();

    // 静态初始化块
    static {
        // Integer 类型操作
        MATH_OPERATIONS.put(Integer.class, new GenericMathInner<>(
                Integer::sum,
                (a, b) -> a - b,
                (a, b) -> a * b,
                (a, b) -> {
                    if (b == 0) throw new ArithmeticException("Division by zero");
                    return a / b;
                },
                (a, b) -> Integer.compare(a, b) > 0
        ));

        // Double 类型操作
        MATH_OPERATIONS.put(Double.class, new GenericMathInner<>(
                Double::sum,
                (a, b) -> a - b,
                (a, b) -> a * b,
                (a, b) -> {
                    if (b == 0) throw new ArithmeticException("Division by zero");
                    return a / b;
                },
                (a, b) -> Double.compare(a, b) > 0
        ));
    }

    // 根据类型获取对应的 GenericMathInner 实例
    @SuppressWarnings("unchecked")
    public static <T> GenericMathInner<T> getGenericMathByType(Class<T> type) {
        return (GenericMathInner<T>) MATH_OPERATIONS.get(type);
    }

    // 数学操作接口
    @FunctionalInterface
    interface MathOperation<T> {
        T apply(T a, T b);
    }

    // 数学操作接口
    @FunctionalInterface
    interface MathBooleanOperation<T> {
        boolean apply(T a, T b);
    }

    // 内部类：泛型数学操作实现
    static class GenericMathInner<T> {
        private final MathOperation<T> addition;
        private final MathOperation<T> subtraction;
        private final MathOperation<T> multiplication;
        private final MathOperation<T> division;
        private final MathBooleanOperation<T> biggerThen;

        public GenericMathInner(MathOperation<T> addition,
                                MathOperation<T> subtraction,
                                MathOperation<T> multiplication,
                                MathOperation<T> division,
                                MathBooleanOperation<T> biggerThen) {
            this.addition = addition;
            this.subtraction = subtraction;
            this.multiplication = multiplication;
            this.division = division;
            this.biggerThen = biggerThen;
        }

        public T add(T a, T b) {
            return addition.apply(a, b);
        }

        public T subtract(T a, T b) {
            return subtraction.apply(a, b);
        }

        public T multiply(T a, T b) {
            return multiplication.apply(a, b);
        }

        public T divide(T a, T b) {
            return division.apply(a, b);
        }

        public boolean biggerThen(T a, T b) {
            return biggerThen.apply(a, b);
        }
    }

    public static void main(String[] args) {
        // 使用 Integer 类型
        GenericMathInner<Integer> intMath = GenericMath.getGenericMathByType(Integer.class);
        System.out.println("Integer Add: " + intMath.add(10, 5));
        System.out.println("Integer Divide: " + intMath.divide(20, 4));

        // 使用 Double 类型
        GenericMathInner<Double> doubleMath = GenericMath.getGenericMathByType(Double.class);
        System.out.println("Double Multiply: " + doubleMath.multiply(2.5, 4.2));
        System.out.println("Double Subtract: " + doubleMath.subtract(10.5, 3.2));
    }
}
