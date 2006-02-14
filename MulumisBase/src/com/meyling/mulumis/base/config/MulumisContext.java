// $Id$
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
//     http://www.gnu.org/licenses/lgpl.html
//
// If you didn't download this code from the following link, you should
// check if you aren't using an obsolete version:
//     http://sourceforge.net/projects/mulumis
//
// The hompage of the simulum project is:
//     http://www.mulumis.meyling.com

package com.meyling.mulumis.base.config;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.meyling.mulumis.base.common.AbstractGravityFactory;
import com.meyling.mulumis.base.log.Trace;
import com.meyling.mulumis.base.simulator.GravityEngineFactory;

/**
 * Context for global settings.
 *
 * @version $Revision$
 * @author  Michael Meyling
 */
public class MulumisContext {

    static Map engines = new HashMap();
    
    private MulumisContext() {
        // nothing to do
    }
    
    public static AbstractGravityFactory getAbstractGravityFactory() {
        final String gravityFactoryClassName = System.getProperty("mulumis.GravityFactory");
        if (gravityFactoryClassName == null) {
            return new GravityEngineFactory();
        }
        if (engines.containsKey(gravityFactoryClassName)) {
            return (AbstractGravityFactory) engines.get(gravityFactoryClassName);
        }
        final Class gravityFactoryClass;
        final String method = "getAbstractGravityFactory";
        try {
            gravityFactoryClass = Class.forName(gravityFactoryClassName);
        } catch (ClassNotFoundException e) {
            Trace.trace(MulumisContext.class, method, e);
            throw new RuntimeException("AbstractGravityFactory class \"" 
                + gravityFactoryClassName + "\" not found.", e);
        }
        final Constructor constructor;
        try {
            constructor = gravityFactoryClass.getConstructor(new Class[0]);
        } catch (Exception e) {
            Trace.trace(MulumisContext.class, method, e);
            throw new RuntimeException("No default constructor found for \""
                + gravityFactoryClassName + "\"");
        }
        final Object gravityFactory;
        try {
            gravityFactory =  constructor.newInstance(new Object[0]);
        } catch (Exception e) {
            Trace.trace(MulumisContext.class, method, e);
            throw new RuntimeException("Couldn't create AbstractGravityFactory class \"" 
                    + gravityFactoryClassName + "\".", e);
        }
        engines.put(gravityFactoryClassName, gravityFactory);
        return (AbstractGravityFactory) gravityFactory;
/*        
        final Method create;
        try {
            create = gravityFactory.getClass().getDeclaredMethod("createGravity", new Class[0]);
        } catch (Exception e) {
            Trace.trace(Context.class, method, e);
            throw new RuntimeException("Couldn't find method \"createGravity()\" within class \"" 
                    + gravityFactoryClassName + "\".", e);
        }
        final gravityF
        try {
            final Object result = create.invoke(gravityFactory, new Object[0]);
        } catch (Exception e) {
            Trace.trace(Context.class, method, e);
            throw new RuntimeException("Couldn't find method \"createGravity()\" within class \"" 
                    + gravityFactoryClassName + "\".", e);
        }
 */
    }
    
 }
