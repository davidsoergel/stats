/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public abstract class VariableWidthHistogram1D extends Histogram1D
	{
	// ------------------------------ FIELDS ------------------------------

	double[] lowerBoundaries;// inclusive at the bottom, exclusive at the top


	// --------------------------- CONSTRUCTORS ---------------------------

	public VariableWidthHistogram1D(double from, double to, int bins) //throws StatsException
		{
		super(from, to, bins);
		lowerBoundaries = new double[bins + 1];
		lowerBoundaries[0] = from;
		lowerBoundaries[bins] = to;
		}

	// -------------------------- OTHER METHODS --------------------------

	public int findBinNumber(double x) throws StatsException
		{
		if (x < from || x > to)
			{
			throw new StatsException("Requested number  " + x + " not in histogram range " + from + " - " + to);
			}
		for (int i = 0; i < bins; i++)
			{
			if (x >= lowerBoundaries[i] && x < lowerBoundaries[i + 1])
				{
				return i;
				}
			}
		throw new Error("Impossible");
		}

	public double bottomOfBin(int bin) throws StatsException
		{
		if (bin < 0 || bin >= bins)
			{
			throw new StatsException("Requested bin " + bin + " not in histogram range 0 - " + (bins - 1));
			}
		return lowerBoundaries[bin];
		}

	public double getBoundary(int i)
		{
		return lowerBoundaries[i];
		}

	public double topOfBin(int bin) throws StatsException
		{
		if (bin < 0 || bin >= bins)
			{
			throw new StatsException("Requested bin " + bin + " not in histogram range 0 - " + (bins));
			}
		return lowerBoundaries[bin + 1];
		}
	}
