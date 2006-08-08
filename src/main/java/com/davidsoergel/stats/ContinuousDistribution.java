package com.davidsoergel.stats;

/**
 * @author lorax
 * @version 1.0
 */
public interface ContinuousDistribution
	{
	public int getDimensionality();

	public double[] sample() throws DistributionException;
	}
