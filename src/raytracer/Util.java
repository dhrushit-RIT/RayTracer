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
        // if (p1.getSpace() != p2.getSpace()) {
        // System.out.println("unmatched spaces");
        // }
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

        Vector normalDir = new Vector(normal);
        // normalDir = Util.scale(normalDir, -1);

        double nDotL = Util.dot(lightDir, normalDir);

        Vector ret = new Vector(subtract(
                Util.scale(normal, 2 * nDotL),
                lightDir));

        ret.normalize();

        return ret;
    }

    public static Vector reflect2(Vector incomingVector, Vector normal, Point intersectionPoint) {
        // TODO: optimize this by setting the vectors to always be wrt camera and
        // normalized so you do not have to do that separately

        // assumptions:
        // light direction is from intersection point to light light ------> vPos
        // normal is from intersection point towards away from object
        // reflected ray is from intersection point towards away from normal

        Vector normalDir = new Vector(normal);

        double nDotL = Util.dot(incomingVector, normalDir);

        Vector ret = new Vector(subtract(
            incomingVector, Util.scale(normal, 2 * nDotL)));

        ret.normalize();

        return ret;
    }

    public static Irradiance multColor(double d, Irradiance c) {
        if (c.normalized == false) {
            System.out.println("color is not normalized");
        }
        Irradiance retColor = new Irradiance(c);
        retColor.r *= d;
        retColor.g *= d;
        retColor.b *= d;

        retColor.r = Math.min(255, retColor.r);
        retColor.g = Math.min(255, retColor.g);
        retColor.b = Math.min(255, retColor.b);

        return retColor;
    }

    public static Irradiance multColor(Irradiance c1, Irradiance c2) {
        Irradiance normalizedC1 = c1;
        if (!c1.normalized) {
            normalizedC1 = c1.getNormalized();
        }

        Irradiance normalizedC2 = c2;
        if (!c2.normalized) {
            normalizedC2 = c2.getNormalized();
        }

        Irradiance retColor = new Irradiance(
                normalizedC1.r * normalizedC2.r,
                normalizedC1.g * normalizedC2.g,
                normalizedC1.b * normalizedC2.b,
                true);

        return retColor;
    }

    public static Irradiance multColor(Irradiance c, double d) {
        c.r *= d;
        c.g *= d;
        c.b *= d;

        c.r = Math.min(255, c.r);
        c.g = Math.min(255, c.g);
        c.b = Math.min(255, c.b);

        return c;
    }

    public static Irradiance addColor(Irradiance... colors) {
        Irradiance retColor = new Irradiance(0, 0, 0, true);

        for (Irradiance c : colors) {
            retColor.r += c.r;
            retColor.g += c.g;
            retColor.b += c.b;
        }

        retColor.r = Math.max(0, Math.min(255, retColor.r));
        retColor.g = Math.max(0, Math.min(255, retColor.g));
        retColor.b = Math.max(0, Math.min(255, retColor.b));
        return retColor;
    }

    public static double distance(Point p1, Point p2) {
        // TODO: check the space eace point belongs to before calculating the distance
        return Math.sqrt(Util.sqDistance(p1, p2));
    }

    public static double sqDistance(Point p1, Point p2) {
        // TODO: check the space eace point belongs to before calculating the distance
        double xDiff = p1.x - p2.x;
        double yDiff = p1.y - p2.y;
        double zDiff = p1.z - p2.z;
        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
    }

    public static Irradiance scaleColor(double scaleFactor, Irradiance color) {
        Irradiance retColor = new Irradiance(color);
        retColor.r *= scaleFactor;
        retColor.g *= scaleFactor;
        retColor.b *= scaleFactor;

        return retColor;
    }
}
