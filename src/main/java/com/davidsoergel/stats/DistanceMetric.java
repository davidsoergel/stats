/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

/**
 * A dissimilarity measure that is also a distance in an inner product space, i.e. it is linear and honors the triangle
 * inequality.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface DistanceMetric<T> extends DissimilarityMeasure<T>
	{
	/**
	 * must be symmetric
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	double distanceFromTo(T a, T b);

	//void prepare(Set<T> allLabels);
	}
