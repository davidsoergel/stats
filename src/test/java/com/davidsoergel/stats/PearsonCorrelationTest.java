/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

import com.davidsoergel.dsutils.math.MathUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 */
public class PearsonCorrelationTest {

    @BeforeMethod
    public void setUp()
    {
        
    }

    @Test
    public void firstPointConsidered() throws StatsException {
        double[] a = new double[]{1.,2.,3.,4.};
        double[] b =  new double[]{1000.,2.,3.,4.};
        
        assert(PearsonCorrelation.computeCorrelationCoefficient(a,b) < 1.0);
        
    }

    @Test
    public void lastPointConsidered() throws StatsException {
        double[] a = new double[]{1.,2.,3.,4.};
        double[] b =  new double[]{1.,2.,3.,4000.};

        assert(PearsonCorrelation.computeCorrelationCoefficient(a,b) < 1.0);

    }

    @Test
    public void perfectCorrelationWorks() throws StatsException {
        double[] a = new double[]{1.,2.,3.,4.};
        double[] b =  new double[]{1.,2.,3.,4.};

        // requires fuzzy match due to rounding errors
        assert(MathUtils.equalWithinFPError(PearsonCorrelation.computeCorrelationCoefficient(a,b), 1.0));

    }

}
