package com.davidsoergel.stats;

import org.apache.log4j.Logger;

/**
 * @author lorax
 * @version 1.0
 */
public class MultivariateGaussian implements ContinuousDistribution
	{
	private static Logger logger = Logger.getLogger(MultivariateGaussian.class);

	double[] mean;
	double[][] cov;

	public MultivariateGaussian(double[] mean, double[][] cov)
		{

		}

	public int getDimensionality()
		{
		return mean.length;
		}

	public double[] sample() throws DistributionException
		{
		return new double[0];  //To change body of implemented methods use File | Settings | File Templates.
		}
	}
