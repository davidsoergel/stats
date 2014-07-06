/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class EqualWeightHistogram2D
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(EqualWeightHistogram2D.class);
	//int validcounts, totalcounts;

	private EqualWeightHistogram1D theBaseHistogram;
	private List<EqualWeightHistogram1D> thePerBinHistograms = new ArrayList<EqualWeightHistogram1D>();


	// --------------------------- CONSTRUCTORS ---------------------------

	public EqualWeightHistogram2D(SimpleXYSeries xy, int xBins, int yBins) throws StatsException
		{
		this(xy, xBins, yBins, false);
		}

	public EqualWeightHistogram2D(SimpleXYSeries xy, int xBins, int yBins, boolean cumulative) throws StatsException
		{
		theBaseHistogram = new EqualWeightHistogram1D(xBins, xy.getXArray());

		for (int i = 0; i < xBins; i++)
			{
			double xmin = cumulative ? 0 : theBaseHistogram.bottomOfBin(i);
			double xmax = theBaseHistogram.topOfBin(i);
			thePerBinHistograms.add(new EqualWeightHistogram1D(yBins, xy.getYArray(xmin, xmax)));
			}
		}


	// --------------------- GETTER / SETTER METHODS ---------------------

	public EqualWeightHistogram1D getTheBaseHistogram()
		{
		return theBaseHistogram;
		}

	public List<EqualWeightHistogram1D> getThePerBinHistograms()
		{
		return thePerBinHistograms;
		}

	// -------------------------- OTHER METHODS --------------------------

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
