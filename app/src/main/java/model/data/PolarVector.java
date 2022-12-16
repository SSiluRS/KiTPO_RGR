package model.data;

public class PolarVector {
    private double length;
    private double angle;

    public double getAngle() {
        return angle;
    }

    public double getLength() {
        return length;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String toString(){ return ""+getLength()+" "+getAngle();}
}
