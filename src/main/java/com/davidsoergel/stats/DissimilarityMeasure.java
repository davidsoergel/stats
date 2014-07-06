/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

/**
 * A measure of dissimilarity between points of the generic type.  Note the dissimiliarity need not be symmetric or
 * linear, and may not honor the triangle inequality; so there is no guarantee that it is a metric or that there exists
 * an associated dot product.  Really the only constraints are that an object should have zero dissimilarity from
 * itself, and that all dissimilarities are non-negative.
 *
 * @author <a href="mailto:dev.davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface DissimilarityMeasure<T>
	{
	// just use NaN
	//public static final Double UNKNOWN_DISTANCE = 1e308; // Double.MAX_VALUE; triggers MySQL bug # 21497

	// in fact, use NaN directly; using this constant obscures the fact that == doesn't work right and so forth.
//	public static final Double UNKNOWN_DISTANCE = Double.NaN;

	// -------------------------- OTHER METHODS --------------------------

	/**
	 * Computes the distance from the first given point to the second.
	 *
	 * @param a the starting point of type T
	 * @param b the ending point of type T
	 * @return the double distance according to this measure.
	 */
	//public String getName();
	double distanceFromTo(T a, T b);

	/**
	 * Provides some description of this distance measure, such as its name.
	 *
	 * @return a String describing this distance measure.
	 */
	String toString();
	}
