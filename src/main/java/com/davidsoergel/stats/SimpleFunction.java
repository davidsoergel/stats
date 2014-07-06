/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;


/**
 * A function from the reals onto the reals.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public interface SimpleFunction
	{
	/**
	 * Perform the function on the argument.
	 *
	 * @param x the input
	 * @return the output, f(x).
	 */
	double f(double x);
	}
