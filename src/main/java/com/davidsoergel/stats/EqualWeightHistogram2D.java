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

import java.util.ArrayList;
import java.util.List;

/* $Id$ */

/**
 * @Author David Soergel
 * @Version 1.0
 */
public class EqualWeightHistogram2D
	{
	private static Logger logger = Logger.getLogger(EqualWeightHistogram2D.class);
	//int validcounts, totalcounts;

	private EqualWeightHistogram1D theBaseHistogram;
	private List<EqualWeightHistogram1D> thePerBinHistograms = new ArrayList<EqualWeightHistogram1D>();


	public EqualWeightHistogram2D(SimpleXYSeries xy, int xBins, int yBins) throws StatsException
		{
		theBaseHistogram = new EqualWeightHistogram1D(xBins, xy.getXArray());

		for (int i = 0; i < xBins; i++)
			{
			double xmin = theBaseHistogram.bottomOfBin(i);
			double xmax = theBaseHistogram.topOfBin(i);
			thePerBinHistograms.add(new EqualWeightHistogram1D(yBins, xy.getYArray(xmin, xmax)));
			}

		}


	public EqualWeightHistogram1D getTheBaseHistogram()
		{
		return theBaseHistogram;
		}

	public List<EqualWeightHistogram1D> getThePerBinHistograms()
		{
		return thePerBinHistograms;
		}

	public SimpleXYSeries getYBinBoundarySeries(int yBinNumber) throws StatsException
		{
		SimpleXYSeries result = new SimpleXYSeries();
		double i = 1;
		double xBins = theBaseHistogram.getBins();
		for (EqualWeightHistogram1D yBin : thePerBinHistograms)
			{
			result.addPoint(i * (1. / xBins), yBin.topOfBin(yBinNumber));
			i++;
			}
		return result;
		}
	}
