/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

/**
 * Normalize the array so that the values sum to one.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: DoubleArrayNormalizer.java 289 2008-09-23 17:46:56Z soergel $
 */

public class DoubleArrayLinearScaler
		implements DistributionProcessor<DoubleArrayContainer>//extends DoubleArrayProcessor
	{

	double[] minimum;
	//double[] maxima;
	double[] extent;

	public DoubleArrayLinearScaler(double[] minimum, double[] extent)
		{
		this.minimum = minimum;
		this.extent = extent;
		}

	public void process(DoubleArrayContainer distribution) throws DistributionProcessorException

		{
		double[] array = distribution.getArray();
		for (int i = 0; i < array.length; i++)
			{
			double v = array[i];
			array[i] = (v - minimum[i]) / extent[i];
			}
		}
	}
