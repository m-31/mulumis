//$Id$
//
// This file is part of the program suite "Simulum". Simulum deals with
// different simulations of star movements and their visualizations.
//
// Copyright (C) 2006 by Michael Meyling
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
//
// An online version of this licence could be found at:
//    http://www.gnu.org/licenses/lgpl.html
//
// If you didn't download this code from the following link, you should
// check if you aren't using an obsolete version:
//     http://sourceforge.net/projects/mulumis
//
// The hompage of the simulum project is:
//     http://www.mulumis.meyling.com

package com.meyling.mulumis.base.common;

public interface Gravity {

    /**
     * Initialize gravity engine for given field.
     *
     * @param   field   Work on this star field.
     */
    public abstract void init(Field field);

    /**
     * Calculate new star positions and velocities according to current gravity constant and
     * delta t. Afterwards the total impulse has a new value.
     */
    public abstract void calculate();

    /**
     * Get total impulse of last calculated star field.
     *
     * @return  Total impulse.
     */
    public abstract double[] getImpulse();

    /**
     * Set gravity constant.
     *
     * @param   gamma   Gravity constant.
     */
    public abstract void setGamma(double gamma);

    /**
     * Get gravity constant.
     *
     * @return  Gravity constant.
     */
    public abstract double getGamma();

    /**
     * Set delta t. This is a small time unit.
     *
     * @param   deltat  Delta t.
     */
    public abstract void setDeltat(double deltat);

    /**
     * Get delta t. This is a small time unit.
     *
     * @return  Delta t.
     */
    public abstract double getDeltat();

}
