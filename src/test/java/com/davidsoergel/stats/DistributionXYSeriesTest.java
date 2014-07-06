/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;
import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class DistributionXYSeriesTest
	{
	private DistributionXYSeries series;

	@BeforeMethod
	public void setUp()
		{
		series = new DistributionXYSeries();
		series.addPoints(1., DSCollectionUtils.setOf(1., 2., 3., 4., 5.));
		series.addPoints(2., DSCollectionUtils.setOf(1., 2., 3.));
		series.addPoints(3., DSCollectionUtils.setOf(3., 4., 5.));
		series.addPoints(4., DSCollectionUtils.setOf(-.2));
		series.addPoints(6., DSCollectionUtils.setOf(2., 10.5));
		}

	@Test
	public void addPointUpdatesBounds()
		{
		series.addPoint(13., 15.);
		assert series.getXMax() == 13.;
		assert series.getXMin() == 1.;
		assert series.getYMax() == 15;
		assert series.getYMin() == -.2;
		}

	@Test
	public void addMultipleYPerXWorks()
		{
		series.addPoints(4., DSCollectionUtils.setOf(-.5, 8., 12.));
		assert series.getYList(3.5, 4.5).size() == 4;
		assert series.getYList(4., 4.).size() == 0;
		}

	@Test
	public void xAndYMinAndMaxAreCorrect()
		{
		assert series.getXMax() == 6.;
		assert series.getXMin() == 1.;
		assert series.getYMax() == 10.5;
		assert series.getYMin() == -.2;
		}

	@Test
	public void uniqueXValuesWorks()
		{
		assert series.uniqueXValues().size() == 5;
		series.addPoint(7.7, 7.7);
		assert series.uniqueXValues().size() == 6;
		series.addPoint(7.7, 8.8);
		assert series.uniqueXValues().size() == 6;
		assert DSCollectionUtils
				.isEqualCollection(series.uniqueXValues(), DSCollectionUtils.setOf(1., 2., 3., 4., 6., 7.7));
		}

	@Test
	public void meanAndStddevAtXWorks()
		{
		assert series.meanYAtX(1.) == 3.;
		assert series.stddevYAtX(1., 3.) == Math.sqrt(2.);

		assert Double.isNaN(series.meanYAtX(1.5));
		assert Double.isNaN(series.stddevYAtX(1.5, 0.));
		}

	@Test
	public void cumulativeMeanAndStddevAtXWorks()
		{
		assert Double.isNaN(series.meanYAtXCumulative(1.));
		assert Double.isNaN(series.stddevYAtXCumulative(1, 3.));

		assert series.meanYAtXCumulative(1.5) == 3.;
		assert series.stddevYAtXCumulative(1.5, 3.) == Math.sqrt(2.);

		double mean = series.meanYAtXCumulative(2.1);
		assert mean == DSArrayUtils.mean(new double[]{1., 2., 3., 4., 5., 1., 2., 3.});

		double stddev = series.stddevYAtXCumulative(2.1, mean);
		assert stddev == DSArrayUtils.stddev(new double[]{1., 2., 3., 4., 5., 1., 2., 3.}, mean);

		series.addPoint(4.2, 4.2);

		mean = series.meanYAtXCumulative(7.1);
		assert mean == DSArrayUtils.mean(new double[]{1., 2., 3., 4., 5., 1., 2., 3., 3., 4., 5., -.2, 4.2, 2., 10.5});

		stddev = series.stddevYAtXCumulative(7.1, mean);
		assert stddev == DSArrayUtils
				.stddev(new double[]{1., 2., 3., 4., 5., 1., 2., 3., 3., 4., 5., -.2, 4.2, 2., 10.5}, mean);
		}

	@Test
	public void binXTilesWorks()
		{
		BinnedXYSeries binned = series.binXTiles(10);
		assert binned.numBins() == 10;
		assert binned.getBinCenter(0) == 1.;
		assert binned.getBinCenter(1) == 1. + binned.getHalfXWidthForBin(0) * 2.;
		}

	@Test
	public void binXQuantilesWorks()
		{
		BinnedXYSeries binned = series.binXQuantiles(10);
		assert binned.numBins() == 10;
		}

	@Test
	public void getYListWorks()
		{
		assert DSCollectionUtils
				.isEqualCollection(series.getYList(2, 4), DSCollectionUtils.listOf(1., 2., 3., 3., 4., 5.));
		assert DSCollectionUtils
				.isEqualCollection(series.getYList(1.5, 4.5), DSCollectionUtils.listOf(1., 2., 3., 3., 4., 5., -.2));

		// test special case where we include the max X value
		assert DSCollectionUtils
				.isEqualCollection(series.getYList(2, 6), DSCollectionUtils.listOf(1., 2., 3., 3., 4., 5., -.2));
		assert DSCollectionUtils.isEqualCollection(series.getYList(1.5, 6.5),
		                                           DSCollectionUtils.listOf(1., 2., 3., 3., 4., 5., -.2, 2., 10.5));
		}


	@Test
	public void sizeWorks()
		{
		assert series.size() == 14;
		}

	@Test
	public void allowsDuplicatePoints()
		{
		series.addPoint(7.7, 7.7);
		series.addPoint(7.7, 7.7);
		series.addPoint(7.7, 7.7);

		assert series.size() == 17;
		DSCollectionUtils.isEqualCollection(series.getYList(7.6, 7.8), DSCollectionUtils.listOf(7.7, 7.7, 7.7));
		}

	@Test
	public void asSimpleXYSeriesWorks() throws StatsException
		{
		assert series.asSimpleXYSeries().size() == 14;
		}
	}
