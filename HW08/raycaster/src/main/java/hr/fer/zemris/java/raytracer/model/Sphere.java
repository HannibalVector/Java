package hr.fer.zemris.java.raytracer.model;

/**
 * Implements 3D model of a sphere.
 * @author ilijan
 */
public class Sphere extends GraphicalObject {
    /** Sphere center. */
    private Point3D center;
    /** Sphere radius. */
    private double radius;
    /** Coefficients used for shading. */
    private double kdr, kdg, kdb, krr, krg, krb, krn;

    /**
     * Constructor sets center and radius and shading coefficients.
     * @param center    center of the sphere.
     * @param radius    radius of the sphere.
     * @param kdr       red diffuse component.
     * @param kdg       green diffuse component.
     * @param kdb       blue diffuse component.
     * @param krr       red reflective component.
     * @param krg       green reflective component.
     * @param krb       blue reflective component.
     * @param krn       parameter {@code n} for reflective component.
     */
    public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb, double krn) {
        this.center = center;
        this.radius = radius;
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.krr = krr;
        this.krg = krg;
        this.krb = krb;
        this.krn = krn;
    }

    /**
     * Represents an intersection with a sphere.
     */
    public class SphereIntersection extends RayIntersection {

        /**
         * Constructor for intersection with a sphere.
         *
         * @param point    point of intersection between ray and sphere.
         * @param distance distance between start of ray and intersection.
         * @param outer    specifies if intersection is outer.
         */
        protected SphereIntersection(Point3D point, double distance, boolean outer) {
            super(point, distance, outer);
        }

        @Override
        public Point3D getNormal() {
            return getPoint().sub(center).normalize();
        }

        @Override
        public double getKdr() {
            return kdr;
        }

        @Override
        public double getKdg() {
            return kdg;
        }

        @Override
        public double getKdb() {
            return kdb;
        }

        @Override
        public double getKrr() {
            return krr;
        }

        @Override
        public double getKrg() {
            return krg;
        }

        @Override
        public double getKrb() {
            return krb;
        }

        @Override
        public double getKrn() {
            return krn;
        }
    }

    /**
     * Finds closest intersection with the given sphere.
     * @param ray   ray to check for intersection.
     * @return      {@link RayIntersection} object representing intersection.
     */
    public RayIntersection findClosestRayIntersection(Ray ray) {
        // see: http://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection

        Point3D CO = ray.start.sub(center);
        Point3D l = ray.direction;
        double scalarProduct = l.scalarProduct(CO);
        double root = Math.pow(scalarProduct, 2) - CO.scalarProduct(CO) + radius * radius;

        double distance = -scalarProduct;
        if (root < 0) {
            return null;
        } else if (root > 0) {
            root = Math.sqrt(root);
            distance = distance - root;
        }
        Point3D pointOfIntersection = pointAlongRay(ray, distance);
        return new SphereIntersection(pointOfIntersection, distance, true);
    }

    /**
     * Returns the point on a ray displaced by given distance from origin of the ray,
     * along the direction of the ray.
     * @param ray   ray on which we seek the point.
     * @param d     distance from origin of the ray to desired point.
     * @return      desired point.
     */
    private Point3D pointAlongRay(Ray ray, double d) {
        return ray.start.add(ray.direction.scalarMultiply(d));
    }
}

