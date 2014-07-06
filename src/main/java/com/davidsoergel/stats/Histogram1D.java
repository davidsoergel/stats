/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;
import org.apache.log4j.Logger;

import java.util.Set;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public abstract class Histogram1D //extends SimpleXYSeries
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(Histogram1D.class);

	double validcounts;
	double totalcounts;

	private double[] counts;
	private double[] cumulativeCounts;

	protected int bins;
	//protected int underflow, overflow;

	protected double from, to;

	private double totalsum = 0;// handy to keep the sum around to get the mean quickly


	// --------------------------- CONSTRUCTORS ---------------------------

	public Histogram1D(double from, double to, int bins)// throws StatsException
		{
		if (from > to)
			{
			throw new StatsRuntimeException("Can't build a histogram with negative width from " + from + " to " + to);
			}
		this.from = from;
		this.to = to;
		this.bins = bins;
		counts = new double[bins];
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public double[] getCounts()
		{
		// PERF
		return counts.clone();
		}

	// -------------------------- OTHER METHODS --------------------------

	public void addXValues(SimpleXYSeries s)
		{
		for (double d : s.getXArray())
			{
			add(d);
			}
		}

	public void add(double x)
		{
		try
			{
			counts[findBinNumber(x)]++;
			validcounts++;
			}
		catch (StatsException e)
			{
			// out of range
			}
		totalsum += x;
		totalcounts++;
		invalidateDerived();
		}


	public void add(double x, double repetitions)
		{
		try
			{
			counts[findBinNumber(x)] += repetitions;
			validcounts += repetitions;
			}
		catch (StatsException e)
			{
			// out of range
			}
		totalsum += x * repetitions;
		totalcounts += repetitions;
		invalidateDerived();
		}

	protected void incrementBin(int bin)
		{
		counts[bin]++;
		invalidateDerived();
		}

	public abstract int findBinNumber(double x) throws StatsException;

	public void addXValues(Set<SimpleXYSeries> ss)
		{
		for (SimpleXYSeries s : ss)
			{
			addXValues(s);
			}
		}

	public void addYValues(SimpleXYSeries s)
		{
		for (double i : s.getYArray())
			{
			add(i);
			}
		}

	public void addYValues(Set<SimpleXYSeries> ss)
		{
		for (SimpleXYSeries s : ss)
			{
			addYValues(s);
			}
		}

	public double approximateStdDev()
		{
		double mean = mean();
		double var = 0;
		for (int i = 0; i < counts.length; i++)
			{
			double d = 0;
			try
				{
				d = centerOfBin(i) - mean;
				}
			catch (StatsException e)
				{
				logger.error("Error", e);
				throw new Error("Impossible");
				}
			var += counts[i] * d * d;
			}
		var /= validcounts;
		return Math.sqrt(var);
		}

	//private double sum = 0;
	/*	public double approximateMean()
		 {
		 //int i = 0;
		 if(mean == 0)
			 {

		 for (int i = 0; i < counts.length; i++)
			 {
			 mean += counts[i] * centerOfBin(i);
			 }
		 mean /= validcounts;
			 }
		 return mean;
		 }
 */

	/**
	 * The mean of all the numbers that have been added to this histogram, whether or not they were in range
	 *
	 * @return
	 */
	public double mean()
		{
		return totalsum / totalcounts;
		}

	public double centerOfBin(int i) throws StatsException
		{
		return (topOfBin(i) + bottomOfBin(i)) / 2.0;
		}


	public double halfWidthOfBin(int i) throws StatsException
		{
		return (topOfBin(i) - bottomOfBin(i)) / 2.0;
		}

	public abstract double topOfBin(int bin) throws StatsException;

	public abstract double bottomOfBin(int bin) throws StatsException;

	public double[] getBinCenters() throws StatsException
		{
		double[] result = new double[counts.length];
		for (int i = 0; i < counts.length; i++)
			{
			result[i] = centerOfBin(i);
			}
		return result;
		}

	public double[] getFractions()
		{
		if (totalcounts == 0)
			{
			return null;
			}
		double[] fractions = counts.clone(); //DSArrayUtils.castToDouble(counts);
		DSArrayUtils.multiplyBy(fractions, 1. / totalcounts);
		return fractions;
		}

	public double[] getCumulativeFractions()
		{
		double[] result = getFractions();
		for (int i = 1; i < result.length; i++)
			{
			result[i] += result[i - 1];
			}
		return result;
		}

	private void invalidateDerived()
		{
		cumulativeCounts = null;
		}

	public double[] getCumulativeCounts()
		{
		if (cumulativeCounts == null)
			{
			cumulativeCounts = counts.clone();
			for (int i = 1; i < cumulativeCounts.length; i++)
				{
				cumulativeCounts[i] += cumulativeCounts[i - 1];
				}
			}
		return cumulativeCounts;
		}

	/**
	 * Typically, when fractions are requested, the histogram is just normalized by the number of samples seen so far.
	 * Sometimes we may want to use a different denominator, though.
	 *
	 * @param totalcounts
	 */
	public void setTotalcounts(double totalcounts)
		{
		this.totalcounts = totalcounts;
		}

	public double getCount(int bin)
		{
		return counts[bin];
		}
	}
