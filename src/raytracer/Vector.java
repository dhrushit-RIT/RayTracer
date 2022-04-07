package raytracer;

import org.ejml.simple.SimpleMatrix;

public class Vector {
    public double x;
    public double y;
    public double z;
    private double magnitude;
    private SimpleMatrix matrix;

    public SimpleMatrix getMatrix() {
        return matrix;
    }

    public void setMatrix(SimpleMatrix matrix) {
        this.matrix = matrix;
    }

    public Vector(Vector other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.magnitude = other.magnitude;
        this.matrix = new SimpleMatrix(other.matrix);
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.updateMagnitude();
        this.matrix = new SimpleMatrix(1, 4, true, new double[] { x, y, z, 1.0 });
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public void updateMagnitude() {
        this.setMagnitude((double) Math.sqrt(x * x + y * y + z * z));
    }

    public Vector normalize() {
        if (this.magnitude > 0) {

            this.x /= this.magnitude;
            this.y /= this.magnitude;
            this.z /= this.magnitude;
            // this.updateMagnitude();
            this.magnitude = 1.0; // optimization
        }

        return this;
    }

    public static Vector getVectorFromMatrix(SimpleMatrix matrix) {
        if (matrix.numCols() == 1 && matrix.numRows() >= 3) {
            return new Vector(matrix.get(0, 0) / matrix.get(3, 0), matrix.get(1, 0) / matrix.get(3, 0),
                    matrix.get(2, 0) / matrix.get(3, 0));
        } else if (matrix.numRows() == 1 && matrix.numCols() >= 3) {
            return new Vector(matrix.get(0, 0) / matrix.get(0, 3), matrix.get(0, 1) / matrix.get(0, 3),
                    matrix.get(0, 2) / matrix.get(0, 3));
        } else {
            System.out.println("Not a valid matrix to create a vector");
            return null;
        }
    }

    public String toString() {
        return "V: (" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector){
            Vector other = (Vector)obj;
            return this.x == other.x && this.y == other.y && this.z == other.z && this.magnitude == other.magnitude;
        }
        return false;
    }
}
