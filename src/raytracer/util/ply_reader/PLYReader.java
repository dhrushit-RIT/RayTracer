package raytracer.util.ply_reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import raytracer.Point;
import raytracer.Triangle;

public class PLYReader {
    public static ArrayList<Triangle> loadModel(String filePath) {
        try (BufferedReader br = new BufferedReader(
                new FileReader(filePath))) {
            ArrayList<Point> vList = new ArrayList<>();
            ArrayList<Triangle> fList = new ArrayList<>();
            int numVertices = -1;
            int numFaces = -1;
            boolean actualContent = false;
            String line = br.readLine();
            while (line != null) {
                if (line.contains("end_header")) {
                    actualContent = true;
                } else if (line.contains("element vertex")) {
                    numVertices = Integer.parseInt(line.split("\\s+")[2]);
                } else if (line.contains("element face")) {
                    numFaces = Integer.parseInt(line.split("\\s+")[2]);
                } else if (actualContent) {
                    String[] parts = line.split("\\s+");
                    if (numVertices > 0) {
                        double x = Double.parseDouble(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        double z = Double.parseDouble(parts[2]);
                        vList.add(new Point(x, y, z, Point.Space.WORLD));
                        numVertices--;
                    }else

                    if (numFaces > 0) {
                        int numVerts = Integer.parseInt(parts[0]);
                        int firstVert = Integer.parseInt(parts[1]);
                        int secondVert = Integer.parseInt(parts[2]);
                        int thirdVert = Integer.parseInt(parts[3]);
                        fList.add(new Triangle(null, vList.get(firstVert), new Point[] {
                                vList.get(firstVert),
                                vList.get(secondVert),
                                vList.get(thirdVert)
                        }));
                        numFaces--;
                    }
                }
                line = br.readLine();
            }
            return fList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
