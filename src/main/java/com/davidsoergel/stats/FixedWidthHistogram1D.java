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

/* $Id$ */

package com.davidsoergel.stats;

import org.apache.log4j.Logger;

// ** Redo using matrix library
public class FixedWidthHistogram1D extends Histogram1D
	{
	// ------------------------------ FIELDS ------------------------------

	private static Logger logger = Logger.getLogger(FixedWidthHistogram1D.class);

	private double binwidth;


	// --------------------------- CONSTRUCTORS ---------------------------

	public FixedWidthHistogram1D(double from, double to, double binwidth)
		{
		super(from, to, (int) ((to - from) / binwidth));

		this.binwidth = binwidth;

		counts = new int[bins];
		}

	public FixedWidthHistogram1D(double from, double to, double binwidth, double[] data)
		{
		this(from, to, binwidth);
		for (double d : data)
			{
			add(d);
			}
		}

	// -------------------------- OTHER METHODS --------------------------

	public int bin(double x) throws StatsException
		{
		if (x < from || x > to)
			{
			throw new StatsException("Requested number  " + x + " not in histogram range " + from + " - " + to);
			}
		int result = (int) ((x - from) / binwidth);

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
		return (from + (bin * binwidth));
		}

	public double topOfBin(int bin) throws StatsException
		{
		if (bin < 0 || bin >= bins)
			{
			throw new StatsException("Requested bin " + bin + " not in histogram range 0 - " + (bins - 1));
			}
		return (from + ((bin + 1) * binwidth));
		}
	}
