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

package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class EqualWeightHistogram1D extends VariableWidthHistogram1D
	{
	private static final Logger logger = Logger.getLogger(EqualWeightHistogram1D.class);

	// --------------------------- CONSTRUCTORS ---------------------------

	public EqualWeightHistogram1D(int bins, double[] data) throws StatsException
		{
		this(DSArrayUtils.min(data), DSArrayUtils.max(data), bins, data);
		}


	public EqualWeightHistogram1D(double from, double to, int bins) //throws StatsException
		{
		super(from, to, bins);
		}

	public EqualWeightHistogram1D(double from, double to, int bins, double[] data) throws StatsException
		{
		super(from, to, bins);
		addAll(data);
		}

	private void addAll(double[] data) throws StatsException
		{
		//int bin = 0;
		double pointsPerBin = (double) data.length / (double) bins;

		if (pointsPerBin < 1.)
			{
			//logger.warn(
			throw new StatsException(
					"Can't compute a good histogram for " + bins + " bins with only " + data.length + " data points");
			}

		Arrays.sort(data);
		int prevBin = 0;
		for (int dataIndex = 0; dataIndex < data.length; dataIndex++)
			{
			// cast rounds toward zero, hence == floor
			int bin = (int) ((double) dataIndex / pointsPerBin);
			if (bin >= bins)
				{
				bin = bins - 1;
				}
			if (bin != prevBin)
				{
				lowerBoundaries[bin] = data[dataIndex];
				}
			incrementBin(bin);
			prevBin = bin;
			}

		// obviously the counts should be pointsPerBin for every bin, plus or minus one due to discretizing
		// the main point here was to set the boundaries
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public int getBins()
		{
		return bins;
		}
	}
