package hr.fer.zemris.java.hw12.jvdraw.geometricobjects;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents abstract geometric object. Implements methods for registration and deregistration of listeners,
 * as well as static methods for getting instances of geometric objects from their string representation.
 *
 * @author ilijan
 */
public abstract class GeometricObject {
    /** Name of the object. */
    private String name;
    /** List of listeners observing geometric object for changes. */
    private List<DrawingObjectListener> listeners = new ArrayList<>();
    /** List of properties which can be modified through graphical user interface. */
    protected List<Property> properties = new ArrayList<>();
    /** Type of the object. */
    protected ObjectType type;

    /**
     * Getter for the object name.
     * @return  name of the object.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the object properties.
     * @return  properties of the object.
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Default constructor populates list of properties.
     */
    protected GeometricObject() {
        populateProperties();
    }

    /**
     * Populates list of properties which are modifiable through graphical
     * user interface.
     */
    protected abstract void populateProperties();

    /**
     * Setter for the name.
     * @param name  new name of the object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Draws geometric object using provided {@link Graphics2D} graphics object.
     * @param g {@link Graphics2D} object used for rendering graphics.
     */
    public abstract void draw(Graphics2D g);

    /**
     * Adds {@link DrawingObjectListener} to the collection of listeners.
     * @param l listener to be added.
     */
    public void addDrawingObjectListener(DrawingObjectListener l) {
        listeners.add(l);
    }

    /**
     * Removes {@link DrawingObjectListener} from the collection of listeners.
     * @param l listener to be removed.
     */
    public void removeDrawingObjectListener(DrawingObjectListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all {@link DrawingObjectListener} objects that are subscribed to the object
     * that the object is changed.
     * @param source    object whose change triggers notification to all listeners.
     */
    public void fire(GeometricObject source) {
        listeners.forEach(l -> l.objectChanged(source));
    }

    /**
     * Sets second point when drawing geometric object using two mouse clicks.
     * Object is created using two specified points.
     *
     * @param point second point needed for construction of object using two points.
     */
    public abstract void setSecondPoint(Point point);

    /**
     * Generates string representation of object for storing in vector graphics file.
     * @return  string representation of geometric object.
     */
    public abstract String generateString();

    /**
     * Map of factories which are accessible by object keyword as the key. Used
     * for creating new objects from parsed vector graphic file.
     */
    private static Map<String, FactoryFromArray> factories = populateFactories();

    /**
     * Populates map of factories {@link GeometricObject#factories}.
     * @return  new {@code Map<String, FactoryFromArray>} representing
     *          map of factories.
     */
    private static Map<String,FactoryFromArray> populateFactories() {
        Map<String, FactoryFromArray> constructors = new HashMap<>();

        constructors.put(ObjectType.CIRCLE.getKeyword(),
                params ->
        {
            int center_x = params.get(0);
            int center_y = params.get(1);
            int radius = params.get(2);
            int r = params.get(3);
            int g = params.get(4);
            int b = params.get(5);
            return new Circle(
                    new Point(center_x, center_y),
                    radius,
                    new Color(r, g, b)
            );
        });

        constructors.put(ObjectType.FILLED_CIRCLE.getKeyword(),
                params ->
                {
                    int center_x = params.get(0);
                    int center_y = params.get(1);
                    int radius = params.get(2);
                    int fg_r = params.get(3);
                    int fg_g = params.get(4);
                    int fg_b = params.get(5);
                    int bg_r = params.get(6);
                    int bg_g = params.get(7);
                    int bg_b = params.get(8);
                    return new FilledCircle(
                            new Point(center_x, center_y),
                            radius,
                            new Color(fg_r, fg_g, fg_b),
                            new Color(bg_r, bg_g, bg_b)
                    );
                });

        constructors.put(ObjectType.LINE.getKeyword(),
                params ->
                {
                    int start_x = params.get(0);
                    int start_y = params.get(1);
                    int end_x = params.get(2);
                    int end_y = params.get(3);
                    int r = params.get(4);
                    int g = params.get(5);
                    int b = params.get(6);
                    return new Line(
                            new Point(start_x, start_y),
                            new Point(end_x, end_y),
                            new Color(r, g, b)
                    );
                });

        return constructors;
    }

    /**
     * Interface representing factory which creates new instance of
     * {@link GeometricObject} from list of integer parameters parsed
     * from vector graphics file.
     */
    private interface FactoryFromArray {
        GeometricObject newInstance(List<Integer> parameters);
    }

    /**
     * Factory method creates new {@link GeometricObject} from its string representation.
     * String representation is usually obtained as a single line in vector graphics file and
     * consists of keyword specifying type of geometric object and list of integer parameters.
     * @param string    string representation of new {@link GeometricObject}.
     * @return          new {@link GeometricObject} as specified by its string representation.
     */
    public static GeometricObject fromString(String string) {
        Scanner sc = new Scanner(string);
        String keyword = sc.next();
        String[] parameters = sc.nextLine().trim().split(" +");
        List<Integer> params =
                Arrays.asList(parameters).stream().map(Integer::parseInt).collect(Collectors.toList());

        return factories.get(keyword).newInstance(params);
    }

    /**
     * Returns bounding box, i.e. the smallest rectangle containing given geometric object.
     * @return  bounding box for the geometric object.
     */
    public abstract Rectangle getBoundingBox();

    @Override
    public String toString() {
        return type.getSymbol() + " " + name;
    }
}
