/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;


/**
 * Normalize the array so that the values sum to one.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: DoubleArrayNormalizer.java 289 2008-09-23 17:46:56Z soergel $
 */

public class DoubleArrayNormalizer implements DistributionProcessor<DoubleArrayContainer>//extends DoubleArrayProcessor
	{
	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface DistributionProcessor ---------------------

	public void process(DoubleArrayContainer c)
		{
		double[] counts = c.getArray();
		double total = DSArrayUtils.sum(counts);

		for (int i = 0; i < counts.length; i++)
			{
			counts[i] /= total;
			}
		}
	}
