/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

/**
 * A multi-dimensional distribution over a continuous real space.
 *
 * @author <a href="mailto:dev.davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface ContinuousDistribution
	{
	// -------------------------- OTHER METHODS --------------------------

	/**
	 * Returns the dimensionality of this distribution
	 *
	 * @return the dimensionality of this distribution
	 */
	int getDimensionality();

	/**
	 * Sample the distribution
	 *
	 * @return the double sample
	 */
	double[] sample() throws DistributionException;
	}
