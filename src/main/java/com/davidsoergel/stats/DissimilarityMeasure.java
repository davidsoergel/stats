/*
 * Copyright (c) 2001-2008 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * dev@davidsoergel.com
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the names of any contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
	public static final Double UNKNOWN_DISTANCE = 1e308; // Double.MAX_VALUE; triggers MySQL bug # 21497

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
