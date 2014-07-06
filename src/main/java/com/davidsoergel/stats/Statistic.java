/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;


/**
 * Interface for classes that can compute a double value given an object of the generic type.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public interface Statistic<T>
	{
	// -------------------------- OTHER METHODS --------------------------


	/**
	 * Compute the statistic on the given object.
	 *
	 * @param a the object on which to compute the statistic.
	 * @return the value of the statistic for that object.
	 */
	double measure(T a);

	/**
	 * Provides some description of this statistic, such as its name.
	 *
	 * @return a String describing this statistic.
	 */
	String toString();
	}

