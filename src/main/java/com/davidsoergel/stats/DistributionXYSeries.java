package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Stores a set of y values for each x value, allowing computation of mean, standard deviation, and quantiles for each
 * x.
 * <p/>
 * The reason we don't extend SimpleXYSeries is to avoid redundant storage of the points
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class DistributionXYSeries //extends SimpleXYSeries
	{
	private static final Logger logger = Logger.getLogger(DistributionXYSeries.class);

	// need to store the sorted x's redundantly because there is no KeySortedMultimap
	SortedSet<Double> keys = new TreeSet<Double>();
	Map<Double, Multiset<Double>> yValsPerX = new HashMap<Double, Multiset<Double>>();
//	Map<Double, Set<Double>> yValsPerX = new HashMap<Double, Set<Double>>();

	protected double xMin = Double.POSITIVE_INFINITY;
	protected double xMax = Double.NEGATIVE_INFINITY;
	private double yMin = Double.POSITIVE_INFINITY;
	private double yMax = Double.NEGATIVE_INFINITY;

	// --------------------- GETTER / SETTER METHODS ---------------------

	public double getXMax()
		{
		return xMax;
		}

	public double getXMin()
		{
		return xMin;
		}

	public double getYMin()
		{

		return yMin;
		}

	public double getYMax()
		{
		return yMax;
		}

	//@Override
	public void addPoint(double x, double y) //throws StatsException
		{
		//	super.addPoint(x, y);
		keys.add(x);
		getYMultiset(x).add(y);
		updateXBounds(x);
		updateYBounds(y);
		}

	private void updateXBounds(double x)
		{
		if (x < xMin)
			{
			xMin = x;
			}
		if (x > xMax)
			{
			xMax = x;
			}
		}

	private void updateYBounds(double y)
		{
		if (y < yMin)
			{
			yMin = y;
			}
		if (y > yMax)
			{
			yMax = y;
			}
		}

	public void addPoints(double x, Collection<Double> ys)// throws StatsException
		{
		//	super.addPoint(x, y);
		keys.add(x);
		getYMultiset(x).addAll(ys);
		updateXBounds(x);
		for (Double y : ys)
			{
			updateYBounds(y);
			}
		}

	private Multiset<Double> getYMultiset(double x)
		{
		Multiset<Double> result = yValsPerX.get(x);

		if (result == null)
			{
			result = new HashMultiset<Double>();
			yValsPerX.put(x, result);
			}
		return result;
		}


	// support error bars
	public Set<Double> uniqueXValues()
		{
		return yValsPerX.keySet();
		}

	public double meanYAtX(double x)
		{
		return DSArrayUtils.mean(getYMultiset(x));
		}

	public double stddevYAtX(double x, double mean)
		{
		return DSArrayUtils.stddev(getYMultiset(x), mean);
		}

	public double meanYAtXCumulative(double xTop)
		{
		return DSArrayUtils.mean(getYArray(0, xTop));
		}

	public double stddevYAtXCumulative(double xTop, double mean)
		{
		return DSArrayUtils.stddev(getYArray(0, xTop), mean);
		}

	public BinnedXYSeries makeBinnedXYSeries(double halfBinWidth, double binStep)
		{

		try
			{
			BinnedXYSeries result = new BinnedXYSeries(this);

			//special case for binStep == 0: use current x values as centers

			if (binStep == 0)
				{
				//double halfBinWidth = binWidth / 2.0;
/*
				for (SimpleXYSeries.XYPoint p : points)
					{

					double bottom = p.x - halfBinWidth;
					double top = p.x + halfBinWidth;

					result.addBin(bottom, top, getYArray(bottom, top));
					}
*/
				for (double x : keys)
					{
					//double bottom = x - halfBinWidth;
					//double top = x + halfBinWidth;

					result.addBin(x, halfBinWidth);
					}
				}
			else
				{
				//	double xMin = keys.first();
				//	double xMax = keys.last();
				for (double center = xMin; center < xMax; center += binStep)
					{
					result.addBin(center, halfBinWidth);//, getYArray(bottom, top));
					}
				}
			/*
			for (Double x : keys)
				{
				result.addPointsToBins(x, yValsPerX.get(x));  // forget the original x value; collapse all the points to the bin center
				}
				*/
			return result;
			}
		catch (DistributionException e)
			{
			logger.error("Error", e);
			throw new Error(e);
			}
		}

	public BinnedXYSeries binXTiles(int numBins)
		{
		//	double xMin = keys.first();
		//	double xMax = keys.last();
		double halfBinWidth = ((xMax - xMin) / numBins) / 2.0;
		return makeBinnedXYSeries(halfBinWidth, halfBinWidth);
		}

	public BinnedXYSeries binXQuantiles(int numQuantiles)
		{
		try
			{
			BinnedXYSeries result = new BinnedXYSeries(this);

			EqualWeightHistogram1D theBaseHistogram =
					new EqualWeightHistogram1D(numQuantiles, DSArrayUtils.toPrimitiveArray(yValsPerX.keySet()));

			int numBins = theBaseHistogram.getBins();
			for (int i = 0; i < numBins; i++)
				{
				double bottom = theBaseHistogram.bottomOfBin(i);
				double top = theBaseHistogram.topOfBin(i);
				double center = theBaseHistogram.centerOfBin(i);
				double halfBinWidth = theBaseHistogram.halfWidthOfBin(i);
				result.addBin(center, halfBinWidth);
				/*		for (Double x : keys.tailSet(bottom).headSet(top))
				   {
				   result.addPoints(center, yValsPerX.get(x));  // forget the original x value; collapse all the points to the bin center
				   }*/
				}
			return result;
			}
		catch (DistributionException e)
			{
			logger.error("Error", e);
			throw new Error(e);
			}
		catch (StatsException e)
			{
			logger.error("Error", e);
			throw new Error(e);
			}
		}

	public double[] getYArray(double bottom, double top)
		{
		List<Double> result = new ArrayList<Double>();
		for (Double x : keys.tailSet(bottom).headSet(top))
			{
			result.addAll(yValsPerX.get(x));
			}
		return DSArrayUtils.toPrimitiveArray(result);
		}


	public List<Double> getYList(double bottom, double top)
		{
		List<Double> result = new ArrayList<Double>();
		try
			{
			//bottom = Math.max(bottom, keys.first());  // not necessary


			// here is an annoyance: the headSet(top) call can't ever include keys.last(),
			// because either top == keys.last() in which case it's an open interval,
			// or top > keys.last(), in which case headSet throws an exception because
			// it's operating on a tailSet and top is outside the bounds.


			double last = keys.last();
			top = Math.min(top, last);  // avoid the exception...

			for (Double x : keys.tailSet(bottom).headSet(top))
				{
				// check that a multiset iterator returns the duplicates
				int i = result.size();
				Multiset<Double> multiset = yValsPerX.get(x);
				result.addAll(multiset);
				assert result.size() == i + multiset.size();
				}

			if (top == last)
				{
				result.addAll(yValsPerX.get(top));  // but do include the point
				}
			}
		catch (IllegalArgumentException e)
			{
			// there are no points in the requested range, so we just return an empty list
			}
		return result;
		}

/*
	public Set<Double> getUniqueYForXRange(double bottom, double top)
		{
		// PERF when bins overlap, the points in the intersection are added to the result set repeatedly

		Set<Double> result = new HashSet<Double>();
		Map<Double, Collection<Double>> map = yValsPerX.asMap();
		for (Map.Entry<Double, Collection<Double>> entry : map.entrySet())
			{
			Double key = entry.getKey();
			if (key >= bottom && key < top)
				{
				result.addAll(entry.getValue());
				}
			}
		return result;
		}
*/

	public int size()
		{
		int result = 0;
		for (Multiset<Double> doubles : yValsPerX.values())
			{
			result += doubles.size();
			}
		return result;
		}

	public SimpleXYSeries asSimpleXYSeries() throws StatsException
		{
		SimpleXYSeries result = new SimpleXYSeries();
		for (Double x : keys)
			{
			for (Double y : getYMultiset(x))
				{
				result.addPoint(x, y);
				}
			}
		return result;
		}
	}