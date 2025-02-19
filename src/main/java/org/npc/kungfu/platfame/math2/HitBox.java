package org.npc.kungfu.platfame.math2;

import java.util.ArrayList;

public class HitBox {

    private Vec2 leftTop;
    private double width;
    private double height;

    public HitBox(Vec2 leftTop, double width, double height) {
        this.leftTop = leftTop;
        this.width = width;
        this.height = height;
    }

    public void setLeftTop(double x, double y) {
        this.leftTop = new Vec2(x, y);
    }

    public ArrayList<Vec2> getBoxVectors() {
        ArrayList<Vec2> boxVectors = new ArrayList<>();
        boxVectors.add(leftTop);
        boxVectors.add(new Vec2(leftTop.getX() + width, this.leftTop.getY()));
        boxVectors.add(new Vec2(leftTop.getX() + width, this.leftTop.getY() + height));
        boxVectors.add(new Vec2(leftTop.getX(), this.leftTop.getY() + height));
        return boxVectors;
    }


}
