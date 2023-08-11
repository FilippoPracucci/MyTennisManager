package it.unibo.utils;

import java.io.Serializable;

/**
 * A standard generic {@code Pair<X, Y>}, with getters, hashCode, equals, and toString
 * well implemented.
 * 
 * @param <X> the first value's type
 * @param <Y> the second value's type
 */
public final class Pair<X, Y> implements Serializable {

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
     * Sets the values.
     * 
     * @param x the first value
     * @param y the second value
     */
    public Pair(final X x, final Y y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     *Retrieves the first value.
     * 
     * @return the first value
     */
    public X getX() {
        return x;
    }

    /**
     *Retrieves the second value.
     * 
     * @return the second value
     */
    public Y getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        if (x == null) {
            if (other.x != null) {
                return false;
            }
        } else if (!x.equals(other.x)) {
            return false;
        }
        if (y == null) {
            if (other.y != null) {
                return false;
            }
        } else if (!y.equals(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pair [x=" + x + ", y=" + y + "]";
    }

}
