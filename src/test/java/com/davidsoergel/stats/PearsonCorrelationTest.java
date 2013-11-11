package com.davidsoergel.stats;

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
        // could require fuzzy match due to rounding errors
        
        double[] a = new double[]{1.,2.,3.,4.};
        double[] b =  new double[]{1.,2.,3.,4.};

        assert(PearsonCorrelation.computeCorrelationCoefficient(a,b) == 1.0);

    }

}
