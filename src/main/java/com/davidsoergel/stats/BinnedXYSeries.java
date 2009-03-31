package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Provides distributions of y values within x bins.  Differs from DistributionXYSeries in that this keeps track of bin
 * widths, not just x values (which in this context are taken to be the centers of the bins).
 * <p/>
 * Note that the bins may overlap, in which case a point may contribute to multiple bins.  However, each point is stored
 * only onceAlso, the bins may be of different widths.
 * <p/>
 * The bin "center" is considered to be halfway between the top and the bottom.  Bin widths are generally expressed in
 * terms of "half bin widths", being the distance between the center and the upper and lower bounds.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class BinnedXYSeries //extends DistributionXYSeries
	{
	private SortedMap<Double, Double> xHalfWidthsPerX = new TreeMap<Double, Double>();

	private final DistributionXYSeries basedOnSeries;

	public BinnedXYSeries(DistributionXYSeries basedOnSeries)
		{
		this.basedOnSeries = basedOnSeries;
		}

	/**
	 * Gets the half-width of the bin centered at the given X value.
	 *
	 * @param x
	 * @return the half-width of the bin centered at x
	 * @throws NullPointerException if there is no bin centered at x
	 */
	public double getHalfXWidth(Double x)
		{
		return xHalfWidthsPerX.get(x);
		}

	/**
	 * Computes quantiles of the y values within the bin centered at x
	 *
	 * @param x          the center of the bin to select
	 * @param yQuantiles the number of quantiles into which to divide the y values
	 * @return the quantile distribution of y values within the bin centered at X
	 */
	public EqualWeightHistogram1D getYQuantiles(Double x, int yQuantiles)
		{
		// PERF cache these?
		//double[] yPrimitiveArray =
		//		DSArrayUtils.toPrimitive(yValsPerX.get(x).toArray(DSArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY));

		double[] yPrimitiveArray = DSArrayUtils.toPrimitiveArray(getYList(x));
		if (yPrimitiveArray.length == 0)
			{
			return null;
			}
		return new EqualWeightHistogram1D(yQuantiles, yPrimitiveArray);
		}

	private List<Double> getYList(Double x)
		{
		// PERF cache these

		double halfBinWidth = xHalfWidthsPerX.get(x);
		double bottom = x - halfBinWidth;
		double top = x + halfBinWidth;
		return basedOnSeries.getYList(bottom, top);
		}

	/**
	 * Computes quantiles of the y values within the bin centered at x
	 *
	 * @param x          the center of the bin to select
	 * @param yQuantiles the number of quantiles into which to divide the y values
	 * @return the quantile distribution of y values within the bin centered at X
	 */
	public EqualWeightHistogram1D getYQuantilesCumulative(Double x, int yQuantiles)
		{
		// PERF cache these?
		//double[] yPrimitiveArray =
		//		DSArrayUtils.toPrimitive(yValsPerX.get(x).toArray(DSArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY));

		double xTop = x + xHalfWidthsPerX.get(x);
		double[] yPrimitiveArray = DSArrayUtils.toPrimitiveArray(getYListCumulative(xTop));
		if (yPrimitiveArray.length == 0)
			{
			return null;
			}
		return new EqualWeightHistogram1D(yQuantiles, yPrimitiveArray);
		}

	private List<Double> getYListCumulative(double xTop)
		{
		// PERF cache these
		return basedOnSeries.getYList(0, xTop);
		}

	/**
	 * Creates a new bin at a given X location and width.  We don't allow more than one bin with a given center, but that
	 * constraint is subject to numerical precision issues re equality of double values.
	 *
	 * @param xCenter
	 * @param halfBinWidth
	 * @throws DistributionException
	 */
	public void addBin(double xCenter, double halfBinWidth) throws DistributionException
		{
		// recast {bottom,top} to {center,width} because it makes more sense when storing the values in yValsPerX

		//double binWidth = top - bottom;
		//double xCenter = bottom + binWidth / 2.0;

		if (xHalfWidthsPerX.containsKey(xCenter))
			{
			throw new DistributionException("Can't add the same bin twice");
			}
		xHalfWidthsPerX.put(xCenter, halfBinWidth);
		/*for (double v : yArray)
			{
			yValsPerX.put(xCenter, v);
			}
		*/
		}

	/*	public void add(double x, double y)
		 {
		 double xCenter= xHalfWidthsPerX.subMap(x-)
		 double bottom = xCenter
		 if()

		 yValsPerX.put(xCenter, yVal);
		 }
 */


	public SimpleXYSeries getQuantileSeries(int yQuantiles, int yQuantile, boolean cumulative) throws StatsException
		{
		if (cumulative)
			{
			return getCumulativeQuantileSeries(yQuantiles, yQuantile);
			}
		else
			{
			return getQuantileSeries(yQuantiles, yQuantile);
			}
		}

	public SimpleXYSeries getCumulativeQuantileSeries(int yQuantiles, int yQuantile) throws StatsException
		{
		SimpleXYSeries result = new SimpleXYSeries();
		for (Map.Entry<Double, Double> entry : xHalfWidthsPerX.entrySet())
			{
			double x = entry.getKey();
			EqualWeightHistogram1D hist = getYQuantilesCumulative(x, yQuantiles);
			if (hist == null)
				{
				result.addPoint(x, 0.0);
				}
			else
				{
				double y = hist.topOfBin(yQuantile);
				result.addPoint(x, y);
				}
			}
		return result;
		}

	public SimpleXYSeries getQuantileSeries(int yQuantiles, int yQuantile) throws StatsException
		{
		SimpleXYSeries result = new SimpleXYSeries();
		for (Map.Entry<Double, Double> entry : xHalfWidthsPerX.entrySet())
			{
			double x = entry.getKey();
			EqualWeightHistogram1D hist = getYQuantiles(x, yQuantiles);
			if (hist == null)
				{
				result.addPoint(x, 0.0);
				}
			else
				{
				double y = hist.topOfBin(yQuantile);
				result.addPoint(x, y);
				}
			}
		return result;
		}

	public double countYAtX(double x)
		{
		return getYList(x).size();

//		return yValsPerX.get(x).size();
		}

	public double countYAtXCumulative(double xTop)
		{
		return getYListCumulative(xTop).size();
		//double halfBinWidth = xHalfWidthsPerX.get(x);
		//double bottom = x - halfBinWidth;
		//double top = x + halfBinWidth;
		//return getYForXRange(0, xTop).size();
		}


	/*
	 public void addPointsToBins(Double x, Collection<Double> doubles)
		 {
		 // PERF since the bins may overlap and have different widths, we have no way of knowing which ones get the points without an exhaustive search
		 for (Map.Entry<Double, Double> entry : xHalfWidthsPerX.entrySet())
			 {
			 double center = entry.getKey();
			 double halfBinWidth = entry.getValue();
			 double top = center+  halfBinWidth;
			 double bottom = center = halfBinWidth;
			 if(x >= bottom && x < top)
				 {
				 addPoints(x, doubles);
				 }
			 }
		 }
 */
	public int numBins()
		{
		return xHalfWidthsPerX.size();
		}

	public Set<Double> getBinCenters()
		{
		return xHalfWidthsPerX.keySet();
		}

	public double meanYAtX(Double x)
		{
		return DSArrayUtils.mean(getYList(x));
		}

	public double stddevYAtX(Double x, double mean)
		{
		return DSArrayUtils.stddev(getYList(x), mean);
		}

	public double meanYAtXCumulative(double xTop)
		{
		return DSArrayUtils.mean(getYListCumulative(xTop));
		}

	public double stddevYAtXCumulative(Double xTop, double mean)
		{
		return DSArrayUtils.stddev(getYListCumulative(xTop), mean);
		}

	public int size()
		{
		return basedOnSeries.size();
		}
	}
