/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

// MAYBE Redo using matrix library
public class FixedWidthHistogram1D extends Histogram1D
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(FixedWidthHistogram1D.class);

	private double binWidth;
	private double halfBinWidth;

	public double getHalfBinWidth()
		{
		return halfBinWidth;
		}

	// --------------------------- CONSTRUCTORS ---------------------------

	public FixedWidthHistogram1D(double from, double to, double binWidth) //throws StatsException
		{
		super(from, to, (int) ((to - from) / binWidth));

		this.halfBinWidth = binWidth / 2.0;
		}

	public FixedWidthHistogram1D(double from, double to, double binWidth, double[] data) //throws StatsException
		{
		this(from, to, binWidth);
		for (double d : data)
			{
			add(d);
			}
		}

	public FixedWidthHistogram1D(double[] data, int numBins) //throws StatsException
		{
		// this(data, null, numBins);

		super(DSArrayUtils.min(data), DSArrayUtils.max(data), numBins);
		//numBins = Math.min(numBins, data.length);
		binWidth = (to - from) / numBins;
		halfBinWidth = binWidth / 2.0;
		for (double d : data)
			{
			add(d);
			}
		}

	public FixedWidthHistogram1D(double[] data, double[] weights, int numBins) //throws StatsException
		{
		super(DSArrayUtils.min(data), DSArrayUtils.max(data), numBins);
		//numBins = Math.min(numBins, data.length);
		binWidth = (to - from) / numBins;
		halfBinWidth = binWidth / 2.0;
		for (int i = 0; i < data.length; i++)
			{
			add(data[i], weights == null ? 1.0 : weights[i]);
			}
		}
	// -------------------------- OTHER METHODS --------------------------

	public int findBinNumber(double x) throws StatsException
		{
		if (x < from || x > to)
			{
			throw new StatsException("Requested number  " + x + " not in histogram range " + from + " - " + to);
			}
		int result = (int) ((x - from) / binWidth);

		// edge case where x == xMax; just put it in the top bin
		if (result == bins)
			{
			result--;
			}
		return result;
		}

	public double bottomOfBin(int bin) throws StatsException
		{
		if (bin < 0 || bin >= bins)
			{
			throw new StatsException("Requested bin " + bin + " not in histogram range 0 - " + (bins - 1));
			}
		return (from + (bin * binWidth));
		}

	public double topOfBin(int bin) throws StatsException
		{
		if (bin < 0 || bin >= bins)
			{
			throw new StatsException("Requested bin " + bin + " not in histogram range 0 - " + (bins - 1));
			}
		return (from + ((bin + 1) * binWidth));
		}
	}
