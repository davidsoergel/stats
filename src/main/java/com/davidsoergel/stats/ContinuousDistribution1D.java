/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

/**
 * A one-dimensional continuous distribution on the real number line.
 *
 * @author <a href="mailto:dev.davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface ContinuousDistribution1D
	{
	// -------------------------- OTHER METHODS --------------------------

	/**
	 * Sample the distribution
	 *
	 * @return the double sample
	 */
	double sample();// throws DistributionException;
	}
