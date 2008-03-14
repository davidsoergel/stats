/*
 * Copyright (c) 2001-2007 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * david@davidsoergel.com
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

import com.davidsoergel.dsutils.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Set;

/* $Id$ */

/**
 * @Author David Soergel
 * @Version 1.0
 */
public abstract class Histogram1D extends SimpleXYSeries
	{
	// ------------------------------ FIELDS ------------------------------

	private static Logger logger = Logger.getLogger(Histogram1D.class);

	int validcounts, totalcounts;

	protected int[] counts;
	protected int bins;
	//protected int underflow, overflow;

	protected double from, to;

	private double totalsum = 0;// handy to keep the sum around to get the mean quickly


	// --------------------------- CONSTRUCTORS ---------------------------

	public Histogram1D(double from, double to, int bins)
		{
		this.from = from;
		this.to = to;
		this.bins = bins;
		counts = new int[bins];
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public int[] getCounts()
		{
		return counts;
		}

	// -------------------------- OTHER METHODS --------------------------

	public void addXValues(SimpleXYSeries s)
		{
		for (double i : s.getXArray())
			{
			add(i);
			}
		}

	public void add(double x)
		{
		try
			{
			counts[bin(x)]++;
			validcounts++;
			}
		catch (StatsException e)
			{
			// out of range
			}
		totalsum += x;
		totalcounts++;
		}


	public void add(double x, int repetitions)
		{
		try
			{
			counts[bin(x)] += repetitions;
			validcounts += repetitions;
			}
		catch (StatsException e)
			{
			// out of range
			}
		totalsum += x * repetitions;
		totalcounts += repetitions;
		}

	public abstract int bin(double x) throws StatsException;

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
				logger.debug(e);
				e.printStackTrace();
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
		return (topOfBin(i) + bottomOfBin(i)) / 2;
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
		double[] fractions = ArrayUtils.castToDouble(counts);
		ArrayUtils.multiplyBy(fractions, 1. / totalcounts);
		return fractions;
		}
	}
