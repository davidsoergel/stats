/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import com.davidsoergel.dsutils.math.MersenneTwisterFast;

/**
 * @version $Id$
 */
public class UniformDistribution implements ContinuousDistribution1D
	{
	// ------------------------------ FIELDS ------------------------------

	//static MersenneTwisterFast mtf = new MersenneTwisterFast();
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

	public double sample()//throws DistributionException
		{
		return min + ((max - min) * MersenneTwisterFast.random());
		}

	@Override
	public String toString()
		{
		return "UniformDistribution{" + min + "," + max + '}';
		}
	}
