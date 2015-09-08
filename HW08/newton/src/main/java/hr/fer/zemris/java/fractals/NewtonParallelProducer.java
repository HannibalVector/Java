package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.complex.Complex;
import hr.fer.zemris.java.fractals.complex.ComplexPolynomial;
import hr.fer.zemris.java.fractals.complex.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Producer of data for drawing fractals using Newton-Raphson iterations.
 * @author ilijan
 */
public class NewtonParallelProducer implements IFractalProducer {
    /** Maximum number of iterations. */
    final static int MAX_ITERS = 16;
    /** Threshold for convergence in zero-finding algorithm (Newton's method). */
    final static double CONVERGENCE_THRESHOLD = 0.001;
    /** Threshold for regarding given complex number as known root. */
    final static double ROOT_THRESHOLD = 0.002;
    /** Number of threads for parallel computation. */
    final static int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors() * 8;

    /** Polynomial used for generating fractal. */
    private static ComplexPolynomial polynomial;
    /** Representation of polynomial used for generating fractal using its roots.*/
    private static ComplexRootedPolynomial rootedPolynomial;
    /** Derivative of polynomial representing fractal. */
    private static ComplexPolynomial polynomialDerivative;
    /** Thread pool for parallel execution. */
    private static ExecutorService pool;

    /**
     * Constructor for producer of data for drawing fractals.
     * @param rootedPolynomial  polynomial generating fractal.
     */
    public NewtonParallelProducer(ComplexRootedPolynomial rootedPolynomial) {
        this.rootedPolynomial = rootedPolynomial;
        polynomial = rootedPolynomial.toComplexPolynom();
        polynomialDerivative = polynomial.derive();

        pool = Executors.newFixedThreadPool(NUMBER_OF_THREADS, new ThreadFactory() {
            private int counter = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread newThread = new Thread(r, "newton-" + counter++);
                newThread.setDaemon(true);
                return newThread;
            }
        });
    }

    @Override
    public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo, IFractalResultObserver observer) {
        System.out.println("Započinjem izračune...");
        short[] data = new short[width * height];
        calculateParallel(reMin, reMax, imMin, imMax, width, height, data);
        System.out.println("Izračuni gotovi...");
        observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
        System.out.println("Dojava gotova...");
    }

    /**
     * Parallel implementation calculates fractals using Newton-Raphson iterations.
     *
     * @param reMin  minimal real part.
     * @param reMax  maximal real part.
     * @param imMin  minimal imaginary part.
     * @param imMax  maximal imaginary part.
     * @param width  width of screen for drawing fractal.
     * @param height height of screen for drawing fractal.
     * @param data   array for storage of data.
     */
    public static void calculateParallel(double reMin, double reMax, double imMin, double imMax,
                                  int width, int height, short[] data) {

        class Job implements Callable<Void> {
            int yMin, yMax;
            int width, height;
            double reMin, reMax, imMin, imMax;
            short[] data;

            public Job(int yMin, int yMax, int width, int height,
                       double reMin, double reMax, double imMin, double imMax,
                       short[] data) {
                this.yMin = yMin;
                this.yMax = yMax;
                this.width = width;
                this.height = height;
                this.reMin = reMin;
                this.reMax = reMax;
                this.imMin = imMin;
                this.imMax = imMax;
                this.data = data;
            }

            @Override
            public Void call() throws Exception {
                calculatePart(reMin, reMax, imMin, imMax,
                        width, height, yMin, yMax, data);
                return null;
            }
        }

        List<Future<Void>> jobs = new ArrayList<>();
        int partHeight = height / NUMBER_OF_THREADS;
        int threadBeforeLast = NUMBER_OF_THREADS - 1;
        for (int i = 0; i < threadBeforeLast; i++) {
            jobs.add(pool.submit(new Job(i * partHeight, (i + 1) * partHeight, width, height,
                    reMin, reMax, imMin, imMax, data)));
        }
        jobs.add(pool.submit(new Job(threadBeforeLast * partHeight, height, width, height,
                reMin, reMax, imMin, imMax, data)));

        for (Future<Void> f : jobs) {
            while (true) {
                try {
                    f.get();
                    break;
                } catch (InterruptedException | ExecutionException e) {
                }
            }
        }
    }

    /**
     * Calculates part of data used for drawing fractal. Enables calculation just for subset of all y-coordinates
     * for which fractal will be drawn, and thus enables parallel calculation of fractal data.
     * @param reMin  minimal real part.
     * @param reMax  maximal real part.
     * @param imMin  minimal imaginary part.
     * @param imMax  maximal imaginary part.
     * @param width  width of screen for drawing fractal.
     * @param height height of screen for drawing fractal.
     * @param yMin   minimal y-coordinate on screen.
     * @param yMax   maximal y-coordinate on screen.
     * @param data   array for storage of data.
     */
    public static void calculatePart(double reMin, double reMax, double imMin, double imMax,
                              int width, int height, int yMin, int yMax, short[] data) {

        int offset = yMin * width;
        for (int y = yMin; y < yMax; y++) {
            for (int x = 0; x < width; x++) {
                double startApproxReal = (double) x / width * (reMax - reMin) + reMin;
                double startApproxImag = (double) (height - y) / height * (imMax - imMin) + imMin;
                Complex startApprox = new Complex(startApproxReal, startApproxImag);
                Complex rootApprox = Complex.findRoot(rootedPolynomial::apply, polynomialDerivative::apply,
                        startApprox, MAX_ITERS, CONVERGENCE_THRESHOLD);

                int indexOfClosestRoot = rootedPolynomial.indexOfClosestRootFor(rootApprox, ROOT_THRESHOLD);
                if (indexOfClosestRoot == -1) {
                    data[offset] = 0;
                } else {
                    data[offset] = (short) indexOfClosestRoot;
                }
                offset++;
            }
        }
    }
}