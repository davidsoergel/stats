/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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

	private final boolean failOnTooLittleData;
	// -----------boolean ---------------- CONSTRUCTORS ---------------------------

	public EqualWeightHistogram1D(int bins, double[] data) throws StatsException
		{
		/*if(data.length == 0)
			{
			throw new StatsException("Can't build a histogram from no data");
			}*/
		this(DSArrayUtils.min(data), DSArrayUtils.max(data), bins, data, false);
		}

	public EqualWeightHistogram1D(int bins, double[] data, boolean failOnTooLittleData) throws StatsException
		{
		/*if(data.length == 0)
			{
			throw new StatsException("Can't build a histogram from no data");
			}*/
		this(DSArrayUtils.min(data), DSArrayUtils.max(data), bins, data, failOnTooLittleData);
		}


	public EqualWeightHistogram1D(double from, double to, int bins, boolean failOnTooLittleData) //throws StatsException
		{
		super(from, to, bins);
		this.failOnTooLittleData = false; //
		}

	public EqualWeightHistogram1D(double from, double to, int bins, double[] data, boolean failOnTooLittleData)
			throws StatsException
		{
		super(from, to, bins);
		this.failOnTooLittleData = failOnTooLittleData;
		addAll(data);
		}

	private void addAll(double[] data) throws StatsException
		{
		//int bin = 0;
		double pointsPerBin = (double) data.length / (double) bins;

		if (pointsPerBin < 1. && failOnTooLittleData)
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
