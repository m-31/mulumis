package com.meyling.mulumis.base.simulator;

import com.meyling.mulumis.base.stars.Star;
import com.meyling.mulumis.base.stars.StarField;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.*;

/**
 * Created by m31 on 1/29/17.
 */
public class GravityEngineTest {

    StarField field;
    GravityEngine engine;


    @BeforeMethod
    public void beforeEachTestMethod(Method method){//Parameter are optional
        //May perform some initialization/setup before each test.
        //E.g. Initializing User whose properties may be altered by actual @Test
        System.out.println("\n@BeforeMethod: I run before each test method. Test to be executed is : "+method.getName());
        field = new StarField(0);
        Star s1 = new Star(1, new double[] {1, 0, 0});
        Star s2 = new Star(1, new double[] {-1,0, 0});
        field.addStar(s1);
        field.addStar(s2);
        engine = new GravityEngine();
        engine.init(field);
        engine.setGamma(1);
        engine.setDeltat(0.001);
    }


    @Test
    public void testInit() throws Exception {

    }

    @Test
    public void testCalculate() throws Exception {
        for (int i = 0; i < 3000; i++) {
            engine.calculate();
        }
    }

    @Test
    public void testSetDeltat() throws Exception {

    }

    @Test
    public void testGetDeltat() throws Exception {

    }

    @Test
    public void testSetGamma() throws Exception {

    }

    @Test
    public void testGetGamma() throws Exception {

    }

    @Test
    public void testGetImpulse() throws Exception {

    }

}