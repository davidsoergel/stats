/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import org.apache.commons.collections15.map.MultiKeyMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Stores a list of x,y pairs.
 *
 * @version $Id$
 */
public class HeatmapSeries
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(HeatmapSeries.class);

	//protected SortedSet<XYPoint> points = new TreeSet<XYPoint>();

	private MultiKeyMap<Double, HeatmapPoint> points = new MultiKeyMap<Double, HeatmapPoint>();

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

	public void addPoint(final double x, final double y, final double z, final double startx, final double endx,
	                     final double starty, final double endy) throws StatsException
		{
		if (Double.isNaN(x) || Double.isInfinite(x))
			{
			//throw new StatsException("Invalid x value in HeatmapSeries: " + x);
			logger.warn("Invalid x value in HeatmapSeries: " + x);
			return;
			}
		if (Double.isNaN(y) || Double.isInfinite(y))
			{
			//throw new StatsException("Invalid y value in HeatmapSeries: " + y);
			logger.warn("Invalid y value in HeatmapSeries: " + y);
			return;
			}
		if (Double.isNaN(z) || Double.isInfinite(z))
			{
			//throw new StatsException("Invalid z value in HeatmapSeries: " + z);
			logger.warn("Invalid z value in HeatmapSeries: " + z);
			return;
			}
		points.put(x, y, new HeatmapPoint(x, y, z)); //, startx, endx, starty, endy));
		updateBounds(x, y, z);
		}

	public void incrementPoint(double x, double y, double zIncrement, final double startx, final double endx,
	                           final double starty, final double endy) throws StatsException
		{
		throw new Error("Completely broken");
		}

	public void incrementPoint(double x, double y, double zIncrement) //throws StatsException
		//, final double startx, final double endx,final double starty, final double endy) throws StatsException
		{
		if (Double.isNaN(x) || Double.isInfinite(x))
			{
			//throw new StatsException("Invalid x value in HeatmapSeries: " + x);
			logger.warn("Invalid x value in HeatmapSeries: " + x);
			return;
			}
		if (Double.isNaN(y) || Double.isInfinite(y))
			{
			//throw new StatsException("Invalid y value in HeatmapSeries: " + y);
			logger.warn("Invalid y value in HeatmapSeries: " + y);
			return;
			}
		if (Double.isNaN(zIncrement) || Double.isInfinite(zIncrement))
			{
			//throw new StatsException("Invalid zIncrement value in SimpleXYZSeries: " + zIncrement);
			logger.warn("Invalid zIncrement value in HeatmapSeries: " + zIncrement);
			return;
			}
		HeatmapPoint currentPoint = points.get(x, y);
		if (currentPoint != null)
			{
			currentPoint.z += zIncrement;
			updateBounds(x, y, currentPoint.z);
			}
		else
			{
			points.put(x, y, new HeatmapPoint(x, y, zIncrement)); //, startx, endx, starty, endy));

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
		for (HeatmapPoint p : (Collection<HeatmapPoint>) points.values())
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
		for (HeatmapPoint p : (Collection<HeatmapPoint>) points.values())
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
		for (HeatmapPoint p : (Collection<HeatmapPoint>) points.values())
			{
			result[i] = p.z;
			i++;
			}
		return result;
		}

/*	public double[] getStartXArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for (HeatmapPoint p : (Collection<HeatmapPoint>) points.values())
			{
			result[i] = p.startx;
			i++;
			}
		return result;
		}

	public double[] getEndXArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for (HeatmapPoint p : (Collection<HeatmapPoint>) points.values())
			{
			result[i] = p.endx;
			i++;
			}
		return result;
		}

	public double[] getStartYArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for (HeatmapPoint p : (Collection<HeatmapPoint>) points.values())
			{
			result[i] = p.starty;
			i++;
			}
		return result;
		}

	public double[] getEndYArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for (HeatmapPoint p : (Collection<HeatmapPoint>) points.values())
			{
			result[i] = p.endy;
			i++;
			}
		return result;
		}

		*/

	public int size()
		{
		return points.size();
		}

	public List<HeatmapPoint> getPoints()
		{
		return new ArrayList<HeatmapPoint>(points.values());
		}

	// -------------------------- INNER CLASSES --------------------------

	public static class HeatmapPoint implements Comparable<HeatmapPoint>
		{
		public double x, y, z;
		//, startx, endx, starty, endy;

		public HeatmapPoint(final double x, final double y, final double z)
			//, final double startx, final double endx,  final double starty, final double endy)
			{
			this.x = x;
			this.y = y;
			this.z = z;
			/*	this.startx = startx;
						this.endx = endx;
						this.starty = starty;
						this.endy = endy;*/
			}

		public int compareTo(final HeatmapPoint o)
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
