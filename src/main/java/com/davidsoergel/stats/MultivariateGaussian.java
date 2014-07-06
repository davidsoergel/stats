/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;


/**
 * @version $Id$
 */
public class MultivariateGaussian implements ContinuousDistribution
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(MultivariateGaussian.class);

	double[] mean;
	double[][] cov;


	// --------------------------- CONSTRUCTORS ---------------------------

	public MultivariateGaussian(double[] mean, double[][] cov)
		{
		throw new NotImplementedException();
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface ContinuousDistribution ---------------------

	public int getDimensionality()
		{
		return mean.length;
		}

	public double[] sample() throws DistributionException
		{
		return new double[0];//To change body of implemented methods use File | Settings | File Templates.
		}
	}
