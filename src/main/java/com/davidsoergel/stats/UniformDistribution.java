package com.davidsoergel.stats;

import com.davidsoergel.dsutils.MersenneTwisterFast;

/**
 * @author lorax
 * @version 1.0
 */
public class UniformDistribution implements ContinuousDistribution1D
	{
	// ------------------------------ FIELDS ------------------------------

	static MersenneTwisterFast mtf = new MersenneTwisterFast();
	double min;
	double max;

	// --------------------------- CONSTRUCTORS ---------------------------

	public UniformDistribution(double min, double max)
		{
		this.min = min;
		this.max = max;
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface ContinuousDistribution1D ---------------------

	public double sample() throws DistributionException
		{
		return min + ((max - min) * mtf.nextDouble());
		}
	}
