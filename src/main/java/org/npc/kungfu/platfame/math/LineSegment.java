package org.npc.kungfu.platfame.math;

public class LineSegment<T extends Number> {

    private VectorTwo<T> start;
    private VectorTwo<T> end;

    public static <T extends Number> LineSegment<T> createLineSegment(VectorTwo<T> start, VectorTwo<T> end){
        return new LineSegment<T>(start,end);
    }

    private LineSegment(VectorTwo<T> start, VectorTwo<T> end) {
        this.start = start;
        this.end = end;
    }

    public VectorTwo<T> getStart() {
        return start;
    }

    public VectorTwo<T> getEnd() {
        return end;
    }

}
