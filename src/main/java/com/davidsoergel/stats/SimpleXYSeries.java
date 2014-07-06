/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Stores a list of x,y pairs.
 *
 * @version $Id$
 */
public class SimpleXYSeries
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(SimpleXYSeries.class);

	//protected SortedSet<XYPoint> points = new TreeSet<XYPoint>();

	private List<XYPoint> points = new ArrayList<XYPoint>();

	protected double xMin = Double.POSITIVE_INFINITY;
	protected double xMax = Double.NEGATIVE_INFINITY;
	private double yMin = Double.POSITIVE_INFINITY;
	private double yMax = Double.NEGATIVE_INFINITY;

	public SimpleXYSeries()
		{
		}

	public SimpleXYSeries(String filename) throws IOException, StatsException
		{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		try
			{
			String line;
			int i = 0;
			while ((line = br.readLine()) != null)
				{
				line = line.trim();
				if (line.equals("")) //line.isEmpty())   // JDK 1.5 compatibility
					{
					continue;
					}
				String[] numbers = line.split("[ ,\t]+");
				try
					{
					Double x = new Double(numbers[0]);
					Double y = new Double(numbers[1]);
					addPoint(x, y);
					i++;
					}
				catch (NumberFormatException e)
					{
					throw new NumberFormatException("Could not read line " + i + " of " + filename + ": " + line);
					}
				}
			}
		finally
			{
			br.close();
			}
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public void sortByX()
		{
		Collections.sort(points);
		}

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

	// -------------------------- OTHER METHODS --------------------------

	public void addPoint(double x, double y) throws StatsException
		{
		/*	if (Double.isNaN(x) || Double.isInfinite(x))
		   {
		   //throw new StatsException("Invalid x value in SimpleXYSeries: " + x);
		   logger.warn("Invalid x value in SimpleXYSeries: " + x);
		   return;
		   }
	   if (Double.isNaN(y) || Double.isInfinite(y))
		   {
		   //throw new StatsException("Invalid y value in SimpleXYSeries: " + y);
		   logger.warn("Invalid y value in SimpleXYSeries: " + y);
		   return;
		   }*/
		points.add(new XYPoint(x, y));
		updateBounds(x, y);
		}

	public void removeNaNAndInfinity()
		{
		Set<XYPoint> pointsToRemove = new HashSet<XYPoint>();

		for (XYPoint point : points)
			{
			if (Double.isNaN(point.x) || Double.isInfinite(point.x))
				{
				pointsToRemove.add(point);
				}
			if (Double.isNaN(point.y) || Double.isInfinite(point.y))
				{
				pointsToRemove.add(point);
				}
			}

		points.removeAll(pointsToRemove);
		}

	public void replaceNaNWith(double d)
		{
		for (XYPoint point : points)
			{
			if (Double.isNaN(point.x))
				{
				point.x = d;
				}
			if (Double.isNaN(point.y))
				{
				point.y = d;
				}
			}
		}

	public void replaceInfinityWith(double d)
		{
		for (XYPoint point : points)
			{
			if (Double.isInfinite(point.x))
				{
				point.x = d;
				}
			if (Double.isInfinite(point.y))
				{
				point.y = d;
				}
			}
		}

	private void updateBounds(double x, double y)
		{
		if (x > xMax)
			{
			xMax = x;
			}
		if (y > yMax)
			{
			yMax = y;
			}
		if (x < xMin)
			{
			xMin = x;
			}
		if (y < yMin)
			{
			yMin = y;
			}
		}

	public double getX(int i)
		{
		return points.get(i).x;
		}


	/**
	 * Provides an array of all the x values present in this series, in increasing order.
	 *
	 * @return
	 */
	public double[] getXArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for (XYPoint p : points)
			{
			result[i] = p.x;
			i++;
			}
		return result;
		}

	/**
	 * Provides an array of the unique x values present in this series, in increasing order.
	 *
	 * @return
	 */
	public double[] getUniqueXArray()
		{
		SortedSet<Double> result = new TreeSet<Double>();
		for (XYPoint p : points)
			{
			result.add(p.x);
			}
		return DSArrayUtils.toPrimitive(result.toArray(DSArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY));
		}


	public double getY(int i)
		{
		return points.get(i).y;
		}


	/**
	 * Provides an array of all the y values present in this series, in order of their associated x values.
	 *
	 * @return
	 */
	public double[] getYArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for (XYPoint p : points)
			{
			result[i] = p.y;
			i++;
			}
		return result;
		}

	/**
	 * Provides an array of y values, for x within the given range, in order of their associated x values.
	 *
	 * @return
	 */
	public double[] getYArray(double xmin, double xmax)
		{
		ArrayList<Double> result = new ArrayList<Double>();

		//	int i = 0;

		// PERF use headSet and tailSet

		for (XYPoint p : points)
			{
			//** double-count the edge cases; otherwise a bin containing only one value appears empty
			if (p.x >= xmin && p.x <= xmax)
				{
				//result[i] = p.y;
				result.add(p.y);
				//i++;
				}
			}
		//double[] result = new double[points.size()];
		//return result;
		return DSArrayUtils.toPrimitive(result.toArray(new Double[0]), 0);
		}

	/*	public Iterator getYiterator()
		 {
		 return new Iterator()
		 {
		 int trav = 0;

		 public boolean hasNext()
			 {
			 return trav < points.size();
			 }

		 public Object next()
			 {
			 return points.get(trav++).y;
			 }

		 public void remove()
			 {
			 //To change body of implemented methods use File | Settings | File Templates.
			 }
		 };
		 }
 */

	public int size()
		{
		return points.size();
		}

	public SimpleXYSeries getYSmoothedCopy(double smoothFactor) throws StatsException
		{
		if (smoothFactor == 0)
			{
			return this;
			}
		SimpleXYSeries result = new SimpleXYSeries();

		// PERF inefficient, and it doesn't necessarily make sense to leave the X values intact while smoothing Y
		for (XYPoint p : points)
			{
			result.addPoint(p.x, DSArrayUtils.mean(getYArray(p.x - smoothFactor, p.x + smoothFactor)));
			}

		return result;
		}


	// -------------------------- INNER CLASSES --------------------------

	protected static class XYPoint implements Comparable
		{
		public double x, y;

		public XYPoint(double x, double y)
			{
			this.x = x;
			this.y = y;
			}

		public int compareTo(Object o)
			{
			return Double.compare(x, ((XYPoint) o).x);
			}
		}

	public static SimpleXYSeries yVsY(SimpleXYSeries theSeries1, SimpleXYSeries theSeries2) throws StatsException
		{
		SimpleXYSeries result = new SimpleXYSeries();

		int size = theSeries1.size();

		if (theSeries2.size() != size)
			{
			throw new StatsRuntimeException("Can't make Y vs. Y plot when # of points differ");
			}

		for (int i = 0; i < size; i++)
			{
			result.addPoint(theSeries1.getY(i), theSeries2.getY(i));
			}

		return result;
		}


	LinearRegression regression = null;
	double correlation;

	public double pearsonCorrelation() throws StatsException
		{
		if (regression == null)
			{
			computeRegression();
			}
		return correlation;
		}

	public double R2() throws StatsException
		{
		if (regression == null)
			{
			computeRegression();
			}
		logger.debug("Correlations: " + regression.R2 + ", " + correlation);
		return regression.R2;
		}

	public double regressionM() throws StatsException
		{
		if (regression == null)
			{
			computeRegression();
			}
		return regression.slope;
		}

	public double regressionB() throws StatsException
		{
		if (regression == null)
			{
			computeRegression();
			}
		return regression.intercept;
		}

	private void computeRegression() throws StatsException
		{
		double[] xs = new double[size()];
		double[] ys = new double[size()];
		int i = 0;
		for (XYPoint point : points)
			{
			xs[i] = point.x;
			ys[i] = point.y;
			i++;
			}

		regression = new LinearRegression(xs, ys);
		correlation = PearsonCorrelation.computeCorrelationCoefficient(xs, ys);
		}
	}
