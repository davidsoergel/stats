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
			throw new StatsException("Invalid x value in SimpleXYZSeries: " + x);
			}
		if (Double.isNaN(y) || Double.isInfinite(y))
			{
			throw new StatsException("Invalid y value in SimpleXYZSeries: " + y);
			}
		if (Double.isNaN(z) || Double.isInfinite(z))
			{
			throw new StatsException("Invalid z value in SimpleXYZSeries: " + z);
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

	protected static class XYZPoint implements Comparable
		{
		public double x, y, z;

		public XYZPoint(double x, double y, double z)
			{
			this.x = x;
			this.y = y;
			this.z = z;
			}

		public int compareTo(Object o)
			{
			return Double.compare(x, ((XYZPoint) o).x);
			}
		}
	}
