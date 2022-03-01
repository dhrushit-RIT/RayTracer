import java.util.ArrayList;
import java.util.Iterator;

public class FilmPlane implements Iterable<Pixel> {
    public int height;
    public int width;
    public Vector cPosition; // position wrt camera

    public int numPixelsHeight;
    public int numPixelsWidth;

    private ArrayList<ArrayList<Pixel>> filmPixels;

    public FilmPlane(int width, int height, int numPixelsWidth, int numPixelsHeight, Vector wPosition) {
        this.width = width;
        this.height = height;
        this.cPosition = wPosition;
        this.filmPixels = new ArrayList<ArrayList<Pixel>>();

        this.numPixelsHeight = numPixelsHeight;
        this.numPixelsWidth = numPixelsWidth;

        Pixel.height = (double) height / numPixelsHeight;
        Pixel.width = (double) width / numPixelsWidth;

        for (int row = 0; row < numPixelsHeight; row++) {
            ArrayList<Pixel> pixelRow = new ArrayList<>();
            for (int col = 0; col < numPixelsWidth; col++) {

                double pX = -width / 2 + col * Pixel.width + Pixel.width / 2;
                double pY = -height / 2 + row * Pixel.height + Pixel.height / 2;

                Vector cPixelPos = new Vector(pX, pY, this.cPosition.z);
                // Vector cPixelPos = Util.add(this.cPosition, pixelPositionWrtPlane);

                pixelRow.add(new Pixel(row, col, cPixelPos));
            }
            filmPixels.add(pixelRow);
        }

        System.out.println(this);
    }

    @Override
    public Iterator<Pixel> iterator() {
        return new Iterator<Pixel>() {
            // public int curRow = numPixelsHeight - 1;
            public int curRow = 0;
            public int curCol = 0;

            @Override
            public boolean hasNext() {
                // return curRow >= 0;
                return curRow < numPixelsHeight;
            }

            @Override
            public Pixel next() {
                Pixel ret = filmPixels.get(curRow).get(curCol);

                curCol++;
                if (curCol >= numPixelsWidth) {
                    curCol = 0;
                    curRow++;
                }

                return ret;
            }

        };
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("FILM PLANE : \n");
        sb.append("\tposition " + cPosition + "\n");
        // sb.append("\tposition wrt camera" + Camera.toCameraSpace(cPosition) + "\n");

        return sb.toString();
    }
}
