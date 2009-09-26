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

import java.util.ArrayList;
import java.util.List;
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

	// -------------------------- OTHER METHODS --------------------------

	public void addPoint(double x, double y) throws StatsException
		{
		if (Double.isNaN(x) || Double.isInfinite(x))
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
			}
		points.add(new XYPoint(x, y));
		updateBounds(x, y);
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
