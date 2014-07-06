/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import com.davidsoergel.dsutils.math.MathUtils;
import com.davidsoergel.dsutils.math.MersenneTwisterFast;


/**
 * @version $Id$
 */
public class LogNormalDistribution implements ContinuousDistribution1D
	{
	// ------------------------------ FIELDS ------------------------------

	//static MersenneTwisterFast mtf = new MersenneTwisterFast();
	double mean;
	double variance;


	// --------------------------- CONSTRUCTORS ---------------------------

	public LogNormalDistribution(double m, double var)
		{
		mean = m;
		variance = var;
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface ContinuousDistribution1D ---------------------

	public double sample()//throws DistributionException
		{
		return MathUtils.approximateLog(mean + (variance * MersenneTwisterFast.gaussian()));
		}
	}
