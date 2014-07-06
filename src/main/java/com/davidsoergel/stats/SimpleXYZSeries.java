/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import org.apache.commons.collections15.map.MultiKeyMap;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 * Stores a list of x,y pairs.
 *
 * @version $Id$
 */
public class SimpleXYZSeries
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(SimpleXYZSeries.class);

	//protected SortedSet<XYPoint> points = new TreeSet<XYPoint>();

	private MultiKeyMap<Double, XYZPoint> points = new MultiKeyMap<Double, XYZPoint>();

	protected double xMin = Double.POSITIVE_INFINITY;
	protected double xMax = Double.NEGATIVE_INFINITY;
	private double yMin = Double.POSITIVE_INFINITY;
	private double yMax = Double.NEGATIVE_INFINITY;
	private double zMin = Double.POSITIVE_INFINITY;
	private double zMax = Double.NEGATIVE_INFINITY;

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

	public double getZMax()
		{
		return zMax;
		}

	public double getZMin()
		{
		return zMin;
		}
// -------------------------- OTHER METHODS --------------------------

	public void addPoint(double x, double y, double z) throws StatsException
		{
		if (Double.isNaN(x) || Double.isInfinite(x))
			{
			//throw new StatsException("Invalid x value in SimpleXYZSeries: " + x);
			logger.warn("Invalid x value in SimpleXYZSeries: " + x);
			return;
			}
		if (Double.isNaN(y) || Double.isInfinite(y))
			{
			//throw new StatsException("Invalid y value in SimpleXYZSeries: " + y);
			logger.warn("Invalid y value in SimpleXYZSeries: " + y);
			return;
			}
		if (Double.isNaN(z) || Double.isInfinite(z))
			{
			//throw new StatsException("Invalid z value in SimpleXYZSeries: " + z);
			logger.warn("Invalid z value in SimpleXYZSeries: " + z);
			return;
			}
		points.put(x, y, new XYZPoint(x, y, z));
		updateBounds(x, y, z);
		}

	public void incrementPoint(double x, double y, double zIncrement) throws StatsException
		{
		if (Double.isNaN(x) || Double.isInfinite(x))
			{
			throw new StatsException("Invalid x value in SimpleXYZSeries: " + x);
			}
		if (Double.isNaN(y) || Double.isInfinite(y))
			{
			throw new StatsException("Invalid y value in SimpleXYZSeries: " + y);
			}
		if (Double.isNaN(zIncrement) || Double.isInfinite(zIncrement))
			{
			throw new StatsException("Invalid zIncrement value in SimpleXYZSeries: " + zIncrement);
			}
		XYZPoint currentPoint = points.get(x, y);
		if (currentPoint != null)
			{
			currentPoint.z += zIncrement;
			updateBounds(x, y, currentPoint.z);
			}
		else
			{
			points.put(x, y, new XYZPoint(x, y, zIncrement));

			updateBounds(x, y, zIncrement);
			}
		}

	private void updateBounds(double x, double y, double z)
		{
		if (x > xMax)
			{
			xMax = x;
			}
		if (y > yMax)
			{
			yMax = y;
			}
		if (z > zMax)
			{
			zMax = z;
			}
		if (x < xMin)
			{
			xMin = x;
			}
		if (y < yMin)
			{
			yMin = y;
			}
		if (z < zMin)
			{
			zMin = z;
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
		for (XYZPoint p : (Collection<XYZPoint>) points.values())
			{
			result[i] = p.x;
			i++;
			}
		return result;
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
		for (XYZPoint p : (Collection<XYZPoint>) points.values())
			{
			result[i] = p.y;
			i++;
			}
		return result;
		}


	public double getZ(int i)
		{
		return points.get(i).z;
		}


	/**
	 * Provides an array of all the y values present in this series, in order of their associated x values.
	 *
	 * @return
	 */
	public double[] getZArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for (XYZPoint p : (Collection<XYZPoint>) points.values())
			{
			result[i] = p.z;
			i++;
			}
		return result;
		}

	public int size()
		{
		return points.size();
		}

	// -------------------------- INNER CLASSES --------------------------

	protected static class XYZPoint implements Comparable<XYZPoint>
		{
		public double x, y, z;

		public XYZPoint(double x, double y, double z)
			{
			this.x = x;
			this.y = y;
			this.z = z;
			}

		public int compareTo(final XYZPoint o)
			{
			int c = Double.compare(x, o.x);
			if (c == 0)
				{
				c = Double.compare(y, o.y);
				}
			if (c == 0)
				{
				c = Double.compare(z, o.z);
				}
			return c;
			}
		}
	}
