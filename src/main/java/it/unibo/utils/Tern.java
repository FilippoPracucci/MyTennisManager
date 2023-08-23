package it.unibo.utils;

import java.io.Serializable;

/**
 * A standard generic {@code Tern<X, Y, Z>}, with getters, hashCode, equals, and toString
 * well implemented.
 * 
 * @param <X> the first value's type
 * @param <Y> the second value's type
 * @param <Z> the third value's type
 */
public class Tern<X, Y, Z> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The first value.
     */
    private final X x;

    /**
     * The second value.
     */
    private final Y y;

    /**
     * The third value.
     */
    private final Z z;

    /**
     * Sets the values.
     * 
     * @param x the first value
     * @param y the second value
     */
    public Tern(final X x, final Y y, final Z z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *Retrieves the first value.
     * 
     * @return the first value
     */
    public X getX() {
        return this.x;
    }

    /**
     *Retrieves the second value.
     * 
     * @return the second value
     */
    public Y getY() {
        return this.y;
    }

    /**
     *Retrieves the third value.
     * 
     * @return the third value
     */
    public Z getZ() {
        return this.z;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        result = prime * result + ((z == null) ? 0 : z.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Tern [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

}
