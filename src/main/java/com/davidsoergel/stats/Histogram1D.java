/* $Id$ */

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

import org.apache.log4j.Logger;

import java.util.Set;

// ** Redo using matrix library
public class Histogram1D//extends SimpleXYSeries
	{
	// ------------------------------ FIELDS ------------------------------

	private static Logger logger = Logger.getLogger(Histogram1D.class);
	int validcounts, totalcounts;

	private int[] counts;
	private int maxbin;
	private int underflow, overflow;
	private double from, to, binwidth;

	private double sum = 0;


	// --------------------------- CONSTRUCTORS ---------------------------

	public Histogram1D(double from, double to, double binwidth)
		{
		this.from = from;
		this.to = to;
		this.binwidth = binwidth;
		maxbin = (int) ((to - from) / binwidth);
		counts = new int[maxbin + 1];
		}

	public Histogram1D(double from, double to, double step, double[] data)
		{
		this.from = from;
		this.to = to;
		this.binwidth = step;
		counts = new int[(int) (((to - from) / step) + 1)];
		for (double d : data)
			{
			add(d);
			}
		}

	public void add(double x)
		{
		int b = bin(x);
		if (b < 0)
			{
			underflow++;
			}
		else if (b > maxbin)
			{
			overflow++;
			}
		else
			{
			counts[bin(x)]++;
			validcounts++;
			}
		sum += x;
		totalcounts++;
		}

	public int bin(double x)
		{
		return (int) ((x - from) / binwidth);
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
			double d = centerOfBin(i) - mean;
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

	public double mean()
		{
		return sum / totalcounts;
		}

	public double centerOfBin(int i)
		{
		return from + (i * binwidth) + (0.5 * binwidth);
		}
	}
