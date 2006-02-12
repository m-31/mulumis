package com.meyling.mulumis.base.common;


public interface Field {

    /**
     * Returns the number of stars.
     *
     * @return    Number of stars.
     */
    public abstract int getNumberOfStars();

    /**
     * Returns the mass sum of stars.
     *
     * @return    Mass of stars.
     */
    public abstract double getMass();

    /**
     * Returns the requested star.
     *
     * @param    i    Star number. Between 0 and <code>{@link #getNumberOfStars()} - 1</code>.
     * @return    Requested star.
     */
    public abstract GravityObject getStar(int i);

}