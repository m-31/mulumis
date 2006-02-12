package com.meyling.mulumis.base.common;


public interface Gravity {

    /**
     * Calculate new star positions and velocities according to current gravity constant and
     * delta t. Afterwards the total impulse has a new value.
     * 
     * @param   field   Work on this star field.
     */
    public abstract void calculate(final Field field);

    /**
     * Get gravity constant.
     * 
     * @return  Gravity constant.
     */
    public abstract double getGamma();

    /**
     * Get delta t. This is a small time unit.
     * 
     * @return  Delta t.
     */
    public abstract double getDeltat();

    /**
     * Does this gravity engine has any gravity set?
     * 
     * @return  Gravity is active.
     */
    public abstract boolean hasGravity();

    /**
     * Get total impulse of last calculated star field.
     * 
     * @return  Total impulse.
     */
    public abstract double[] getImpulse();

}