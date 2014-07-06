/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;


/**
 * A distribution that can be represented as an array of double.  Of course, any distribution that can be represented as
 * a set of double parameters can qualify in principle, but here the idea is that the parameters are all equivalent in
 * some sense (i.e., frequencies of samples in each of n classes, etc.).  In this case it may (or may not) be sensible
 * to apply various generic DistributionProcessors (i.e., adding pseudocounts) or DistanceMeasures (i.e., Euclidean
 * distance) to these distributions.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public interface DoubleArrayContainer extends MutableDistribution  //<T extends DoubleArrayContainer>
	{
	// -------------------------- OTHER METHODS --------------------------

	/**
	 * Gets the array of parameters for this distribution
	 *
	 * @return the array of parameters for this distribution
	 */
	double[] getArray();

	/**
	 * The sum of the elements in the array.  It's useful to cache this to avoid recomputing it all the time.
	 *
	 * @return
	 */
	double getArraySum();

	// insist on an implementation
	//T clone();
	Object clone();
	}
