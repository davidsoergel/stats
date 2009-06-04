package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides distributions of y values within x bins.  Differs from DistributionXYSeries in that this keeps track of bin
 * widths, not just x values (which in this context are taken to be the centers of the bins).
 * <p/>
 * Note that the bins may overlap, in which case a point may contribute to multiple bins.  However, each point is stored
 * only once.  Also, the bins may be of different widths.
 * <p/>
 * Basically this just keeps track of a set of predefined vertical slices through the underlying DistributionXYSeries.
 * <p/>
 * The bin "center" is considered to be halfway between the top and the bottom.  Bin widths are generally expressed in
 * terms of "half bin widths", being the distance between the center and the upper and lower bounds.
 * <p/>
 * Bins are identified by an integer id; no guarantees are made about the order of these.  There may be multiple copies
 * of a bin with the same center and width.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class BinnedXYSeries //extends DistributionXYSeries
	{
	//private SortedMap<Double, Double> xHalfWidthsPerX = new TreeMap<Double, Double>();

	private List<Double> xCenters = new ArrayList<Double>();
	private List<Double> xHalfWidths = new ArrayList<Double>();

	private final DistributionXYSeries basedOnSeries;

	public BinnedXYSeries(DistributionXYSeries basedOnSeries)
		{
		this.basedOnSeries = basedOnSeries;
		}

	/**
	 * Gets the half-width of the bin centered at the given X value.
	 *
	 * @param i
	 * @return the half-width of the bin centered at x
	 * @throws NullPointerException if there is no bin centered at x
	 */
	public double getHalfXWidthForBin(int i)
		{
		return xHalfWidths.get(i);
		}


	/**
	 * Computes quantiles of the y values within a given bin
	 *
	 * @param i          the bin index to select
	 * @param yQuantiles the number of quantiles into which to divide the y values
	 * @return the quantile distribution of y values within the requested bin
	 */
	private EqualWeightHistogram1D getYQuantilesForBin(int i, int yQuantiles)
		{
		// PERF cache these?
		//double[] yPrimitiveArray =
		//		DSArrayUtils.toPrimitive(yValsPerX.get(x).toArray(DSArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY));

		double[] yPrimitiveArray = DSArrayUtils.toPrimitiveArray(getYListForBin(i));
		if (yPrimitiveArray.length == 0)
			{
			return null;
			}
		return new EqualWeightHistogram1D(yQuantiles, yPrimitiveArray);
		}

	private List<Double> getYListForBin(int i)
		{
		// PERF cache these

		double x = xCenters.get(i);
		double halfBinWidth = xHalfWidths.get(i);

		if (halfBinWidth == 0.0)
			{
			// increment a hair so that the interval can be open but still include the point
			halfBinWidth += Math.ulp(halfBinWidth);
			}

		double bottom = x - halfBinWidth;
		double top = x + halfBinWidth;

		return basedOnSeries.getYList(bottom, top);
		}

	/**
	 * Computes quantiles of the y values within the bin centered at x
	 *
	 * @param i          the bin index to select
	 * @param yQuantiles the number of quantiles into which to divide the y values
	 * @return the quantile distribution of y values within the requested bin
	 */
	private EqualWeightHistogram1D getYQuantilesForBinCumulative(int i, int yQuantiles)
		{
		// PERF cache these?
		//double[] yPrimitiveArray =
		//		DSArrayUtils.toPrimitive(yValsPerX.get(x).toArray(DSArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY));

		double[] yPrimitiveArray = DSArrayUtils.toPrimitiveArray(getYListForBinRangeToTop(0, i));
		if (yPrimitiveArray.length == 0)
			{
			return null;
			}
		return new EqualWeightHistogram1D(yQuantiles, yPrimitiveArray);
		}

	/**
	 * @param lowBin  inclusive
	 * @param highBin inclusive
	 * @return
	 */
	public List<Double> getYListForBinRangeToTop(int lowBin, int highBin)
		{
		if (highBin == -1)
			{
			return new ArrayList<Double>();
			}
		double highX = xCenters.get(highBin);
		double highHalfBinWidth = xHalfWidths.get(highBin);
		double xTop = highX + highHalfBinWidth;

		double lowX = xCenters.get(lowBin);
		double lowHalfBinWidth = xHalfWidths.get(lowBin);
		double xBottom = lowX - lowHalfBinWidth;

		// PERF cache these?
		return basedOnSeries.getYList(xBottom, xTop);
		}

	/**
	 * @param lowBin  inclusive
	 * @param highBin inclusive of the center point of the high bin
	 * @return
	 */
	public List<Double> getYListForBinRangeToCenter(int lowBin, int highBin)
		{
		double lowX = xCenters.get(lowBin);
		double lowHalfBinWidth = xHalfWidths.get(lowBin);
		double xBottom = lowX - lowHalfBinWidth;

		// PERF cache these?
		double center = xCenters.get(highBin);
		center += Math.ulp(center);
		return basedOnSeries.getYList(xBottom, center);
		}

	/**
	 * inclusive of the center point
	 *
	 * @param bin
	 * @return
	 */
	public List<Double> getYListForBinBottomHalf(final int bin)
		{
		double x = xCenters.get(bin);
		double halfBinWidth = xHalfWidths.get(bin);
		double xBottom = x - halfBinWidth;

		x += Math.ulp(x);
		// PERF cache these?
		return basedOnSeries.getYList(xBottom, x);
		}

	/**
	 * exclusive of the center point
	 *
	 * @param bin
	 * @return
	 */
	public List<Double> getYListForBinTopHalf(final int bin)
		{
		double x = xCenters.get(bin);
		double halfBinWidth = xHalfWidths.get(bin);
		double xTop = x + halfBinWidth;

		x += Math.ulp(x);
		// PERF cache these?
		return basedOnSeries.getYList(x, xTop);
		}

	/**
	 * Creates a new bin at a given X location and width.
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

		/*
		if (halfBinWidth == 0.0)
		 {
			// ** a quantile contains only items of a single value; just skip it
			return;
			}

		if (xHalfWidthsPerX.containsKey(xCenter))
			{
			throw new DistributionException("Can't add the same bin twice: " + xCenter);
			}*/

		//	xHalfWidthsPerX.put(xCenter, halfBinWidth);

		xCenters.add(xCenter);
		xHalfWidths.add(halfBinWidth);
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
		for (int i = 0; i < xCenters.size(); i++)
			{
			double x = xCenters.get(i);
			EqualWeightHistogram1D hist = getYQuantilesForBinCumulative(i, yQuantiles);
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

	/**
	 * Returns a SimpleXYSeries mapping each bin center to the requested Y quantile in that bin
	 *
	 * @param yQuantiles
	 * @param yQuantile
	 * @return
	 * @throws StatsException
	 */
	public SimpleXYSeries getQuantileSeries(int yQuantiles, int yQuantile) throws StatsException
		{
		SimpleXYSeries result = new SimpleXYSeries();
		for (int i = 0; i < xCenters.size(); i++)
			{
			double x = xCenters.get(i);
			EqualWeightHistogram1D hist = getYQuantilesForBin(i, yQuantiles);
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

	public double countYForBin(int i)
		{
		return getYListForBin(i).size();

//		return yValsPerX.get(x).size();
		}

	private double countYForBinCumulative(int i)
		{
		return getYListForBinRangeToTop(0, i).size();
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
		return xCenters.size();
		}

	public List<Double> getBinCenters()
		{
		return xCenters;
		}

	public Double getBinCenter(int i)
		{
		return xCenters.get(i);
		}

	public double meanYForBin(int i)
		{
		return DSArrayUtils.mean(getYListForBin(i));
		}

	public double stddevYForBin(int i, double mean)
		{
		//PERF
		return DSArrayUtils.stddev(getYListForBin(i), mean);
		}

	/*
	 public double meanYAtXCumulative(double xTop)
		 {
		 return DSArrayUtils.mean(getYListCumulative(xTop));
		 }

	 public double stddevYAtXCumulative(Double xTop, double mean)
		 {
		 return DSArrayUtils.stddev(getYListCumulative(xTop), mean);
		 }
 */
	public int size()
		{
		return basedOnSeries.size();
		}
	}
