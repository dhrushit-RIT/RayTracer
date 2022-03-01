public class Pixel {
    public MyColor color;
    public int row;
    public int col;
    public static double width;
    public static double height;
    
    public static double maxR = -1;
    public static double maxG = -1;
    public static double maxB = -1;

    // public Vector planePosition; // with respect to the origin of film plane
    public Vector wPosition; // with respect to the origin of world

    public int divisions = 1;

    public Pixel(int row, int col, Vector position) {
        this.row = row;
        this.col = col;
        this.wPosition = position;
    }

    public void setValue(MyColor value) {
        this.color = value;
        Pixel.maxR = Math.max(maxR, this.color.r);
        Pixel.maxG = Math.max(maxG, this.color.g);
        Pixel.maxB = Math.max(maxB, this.color.b);
    }

    public void setWidth(float width) {
        Pixel.width = width;
    }

    public void setHeight(float height) {
        Pixel.height = height;
    }

    public String toString() {
        return "Pix pos: " + wPosition + " val : " + this.color;
    }

}
