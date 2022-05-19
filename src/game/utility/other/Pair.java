package game.utility.other;

/**
 * A standard generic Pair<X,Y>, with getters, hashCode, equals, toString and clone well implemented.
 * 
 *  @param <X> the first type of the pair
 *  @param <Y> the second type of the pair
 */
public class Pair<X, Y> implements Cloneable {

    private X x;
    private Y y;

    /**
     * @param x the value of the first pair
     * @param y the value of the second pair
     */
    public Pair(final X x, final Y y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * @return the first value of the pair
     */
    public X getX() {
        return x;
    }
    /**
     * @return the second value of the pair
     */
    public Y getY() {
        return y;
    }
    /**
     * Sets the first value of the pair.
     * 
     * @param x the new value
     */
    public void setX(final X x) {
        this.x = x;
    }
    /**
     * Sets the second value of the pair.
     * 
     * @param y the new value
     */
    public void setY(final Y y) {
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
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
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Pair [x=" + x + ", y=" + y + "]";
    }
    /**
     * {@inheritDoc}
     */
    public Pair<X, Y> copy() {
        return new Pair<>(this.getX(), this.getY());
    }

}
