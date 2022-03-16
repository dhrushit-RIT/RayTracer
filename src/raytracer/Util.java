package raytracer;
public class Util {

    public static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static Vector subtract(Point p1, Vector v2) {
        return new Vector(p1.x - v2.x, p1.y - v2.y, p1.z - v2.z);
    }

    public static Vector subtract(Vector v1, Point p2) {
        return new Vector(v1.x - p2.x, v1.y - p2.y, v1.z - p2.z);
    }

    public static Vector subtract(Point p1, Point p2) {
        return new Vector(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
    }

    public static Vector scale(Vector v, double scale) {
        return new Vector(v.x * scale, v.y * scale, v.z * scale);
    }

    public static Vector scale(double scale, Vector v) {
        return new Vector(v.x * scale, v.y * scale, v.z * scale);
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static Vector add(Vector... vs) {
        Vector retVector = new Vector(0, 0, 0);
        for (Vector v : vs) {
            retVector.x += v.x;
            retVector.y += v.y;
            retVector.z += v.z;
        }
        return retVector;
    }

    public static Vector add(Point v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static Vector cross(Vector a, Vector b) {
        return new Vector(
                // 0,0,1 x 1,0,0
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x);
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vector reflect(Point lightPosition, Vector normal, Point intersectionPoint) {
        // TODO: optimize this by setting the vectors to always be wrt camera and
        // normalized so you do not have to do that separately

        // assumptions:
        // light direction is from ligth to intersection point
        // normal is from intersection point towards away from object
        // reflected ray is from intersection point towards away from normal

        Vector ligthDir = new Vector(Util.subtract(intersectionPoint, lightPosition));
        Vector normalDir = new Vector(normal);
        ligthDir.normalize();
        normalDir.normalize();

        double nDotL = Util.dot(ligthDir, normalDir);
        Vector ret = new Vector(subtract(ligthDir, Util.scale(normalDir, 2 * nDotL)));
        ret.normalize();
        return ret;
    }

    public static Vector reflect(Vector lightDir, Vector normal, Point intersectionPoint) {
        // TODO: optimize this by setting the vectors to always be wrt camera and
        // normalized so you do not have to do that separately

        // assumptions:
        // light direction is from intersection point to light vPos ------> light
        // normal is from intersection point towards away from object
        // reflected ray is from intersection point towards away from normal

        double nDotL = Util.dot(lightDir, normal);

        Vector ret = new Vector(subtract(
                Util.scale(normal, 2 * nDotL),
                lightDir));

        ret.normalize();

        return ret;
    }

    public static MyColor multColor(double d, MyColor c) {
        if(c.normalized == false){
            System.out.println("color is not normalized");
        }
        MyColor retColor = new MyColor(c);
        retColor.r *= d;
        retColor.g *= d;
        retColor.b *= d;

        retColor.r = Math.min(255, retColor.r);
        retColor.g = Math.min(255, retColor.g);
        retColor.b = Math.min(255, retColor.b);

        return retColor;
    }

    public static MyColor multColor(MyColor c, double d) {
        c.r *= d;
        c.g *= d;
        c.b *= d;

        c.r = Math.min(255, c.r);
        c.g = Math.min(255, c.g);
        c.b = Math.min(255, c.b);

        return c;
    }

    public static MyColor addColor(MyColor... colors) {
        MyColor retColor = new MyColor(0, 0, 0, true);

        for (MyColor c : colors) {
            retColor.r += c.r;
            retColor.g += c.g;
            retColor.b += c.b;
        }

        retColor.r = Math.max(0, Math.min(255, retColor.r));
        retColor.g = Math.max(0, Math.min(255, retColor.g));
        retColor.b = Math.max(0, Math.min(255, retColor.b));
        return retColor;
    }
}
