package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Implements a simple ray-tracer for drawing 3D scenes.
 * @author ilijan
 */
public class RayCasterParallel {
    /** RGB code for black colour. */
    private static final short[] BLACK = new short[]{0, 0, 0};
    /** Thread pool with support for recursive functions. */
    private static ForkJoinPool pool;

    /**
     * The main method for {@link RayCasterParallel} application.
     * @param args  Command-line arguments. Not used.
     */
    public static void main(String[] args) {
        pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        RayTracerViewer.show(getIRayTracerProducer(),
                new Point3D(10, 0, 0),
                new Point3D(0, 0, 0),
                new Point3D(0, 0, 10),
                20, 20);
    }

    /**
     * Getter for tracer producer - object that implements {@link IRayTracerProducer} interface.
     * @return  instance of {@link IRayTracerProducer} interface used for producing ray-trace.
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return (eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer) ->
                computeParallel(eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer);
    }

    private static void computeParallel(final Point3D eye, Point3D view, Point3D viewUp, final double horizontal, final double vertical, final int width, int height, long requestNo, IRayTracerResultObserver observer) {
        System.out.println("Započinjem izračune...");

        short[] red = new short[width * height];
        short[] green = new short[width * height];
        short[] blue = new short[width * height];

        Point3D eyeView = view.sub(eye);
        Point3D yAxis = viewUp.sub(eyeView.scalarMultiply(eyeView.scalarProduct(viewUp))).normalize();
        Point3D xAxis = eyeView.vectorProduct(yAxis).normalize();
        Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2)).add(yAxis.scalarMultiply(vertical / 2));
        Scene scene = RayTracerViewer.createPredefinedScene();

        class ParallelComputation extends RecursiveAction {
            int yMin;
            int yMax;

            public ParallelComputation(int yMin, int yMax) {
                this.yMin = yMin;
                this.yMax = yMax;
            }

            @Override
            protected void compute() {
                int distance = yMax - yMin;
                if(distance > 200) {
                    ParallelComputation p1 = new ParallelComputation(yMin, (yMin + yMax)/2);
                    ParallelComputation p2 = new ParallelComputation((yMin + yMax)/2, yMax);
                    invokeAll(p1, p2);
                } else {
                    computeDirect();
                }
            }

            private void computeDirect() {
                int offset = yMin*width;

                for (int y = yMin; y < yMax; y++) {
                    for (int x = 0; x < width; x++) {
                        Point3D screenPoint = screenCorner
                                .add(xAxis.scalarMultiply(horizontal * x / (width - 1)))
                                .sub(yAxis.scalarMultiply(vertical * y / (height - 1)));

                        Ray ray = Ray.fromPoints(eye, screenPoint);

                        short[] rgb = tracer(scene, ray);

                        red[offset] = rgb[0] > 255 ? 255 : rgb[0];
                        green[offset] = rgb[1] > 255 ? 255 : rgb[1];
                        blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
                        offset++;
                    }
                }
            }
        }


        ParallelComputation job = new ParallelComputation(0, height);

        pool.invoke(job);

        System.out.println("Izračuni gotovi...");
        observer.acceptResult(red, green, blue, requestNo);
        System.out.println("Dojava gotova...");
    }

    /**
     * Method traces across the screen and colors pixels on screen according to the scene.
     * @param scene scene used for determining colors of pixels.
     * @param ray   ray from observer through given pixel.
     * @return  {@code short[]} containing rgb components of color of pixel.
     */
    private static short[] tracer(Scene scene, Ray ray) {

        RayIntersection closestIntersection = closestIntersection(scene, ray);
        if (closestIntersection == null) {
            return BLACK;
        }

        return determineColorFor(closestIntersection, scene);
    }

    /**
     * Determines colour of pixel at given intersection, depending on positions of light sources
     * and other objects as given in an instance of class {@link Scene}.
     * @param closestIntersection   intersection of interest - defines 3D point inside scene for which
     *                              colour is determined.
     * @param scene                 scene to determine colour from.
     * @return                      rgb code of the colour contained in 3-dim {@code short[]} array.
     */
    private static short[] determineColorFor(RayIntersection closestIntersection, Scene scene) {
        short[] rgb = new short[]{15, 15, 15};
        Iterable<LightSource> lights = scene.getLights();

        for (LightSource light : lights) {
            Ray lightToIntersection = Ray.fromPoints(light.getPoint(), closestIntersection.getPoint());
            RayIntersection lightIntersection = closestIntersection(scene, lightToIntersection);
            if (lightIntersection != null
                    && light.getPoint().sub(lightIntersection.getPoint()).norm() < light.getPoint().sub(closestIntersection.getPoint()).norm()) {
                return rgb;
            } else {
                Point3D intersectionToLight = light.getPoint().sub(closestIntersection.getPoint()).normalize();
                Point3D normal = closestIntersection.getNormal();
                double cosTheta = intersectionToLight.scalarProduct(normal);
                rgb[0] += closestIntersection.getKdr()*light.getR()*cosTheta;
                rgb[1] += closestIntersection.getKdg()*light.getG()*cosTheta;
                rgb[2] += closestIntersection.getKdb()*light.getB()*cosTheta;

                Point3D dir = lightToIntersection.direction;
                Point3D reflected = dir.sub(normal.scalarMultiply(2.0*dir.scalarProduct(normal))).normalize();
                double cosAlphaN = Math.pow(reflected.scalarProduct(normal), closestIntersection.getKrn());

                rgb[0] += closestIntersection.getKrr()*light.getR()*cosAlphaN;
                rgb[1] += closestIntersection.getKrg()*light.getG()*cosAlphaN;
                rgb[2] += closestIntersection.getKrb()*light.getB()*cosAlphaN;
            }
        }

        return rgb;
    }

    /**
     * Finds closest intersection between given ray and given scene, or returns null
     * if no intersection occurs.
     * @param scene     scene to check for intersection with.
     * @param ray       ray to check for intersection with.
     * @return          {@link RayIntersection} object if intersection occurs, or null
     *                  otherwise.
     */
    private static RayIntersection closestIntersection(Scene scene, Ray ray) {
        Iterable<GraphicalObject> objects = scene.getObjects();
        List<RayIntersection> intersections = new ArrayList<>();

        for (GraphicalObject object : objects) {
            intersections.add(object.findClosestRayIntersection(ray));
        }

        RayIntersection closestIntersection = null;
        double closestDistance = 0;
        for (RayIntersection intersection : intersections) {
            if (intersection != null) {
                closestDistance = intersection.getDistance();
                closestIntersection = intersection;
                break;
            }
        }

        if (closestIntersection == null) {
            return null;
        }

        for (RayIntersection intersection : intersections) {
            if (intersection != null) {
                double distance = intersection.getDistance();
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestIntersection = intersection;
                }
            }
        }

        return closestIntersection;
    }
}

