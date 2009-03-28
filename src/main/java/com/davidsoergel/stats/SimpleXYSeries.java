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
import java.util.Iterator;
import java.util.List;

/**
 * @version $Id$
 */
public class SimpleXYSeries
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(SimpleXYSeries.class);

	private List<XYPoint> points = new ArrayList<XYPoint>();

	private double minX = Double.MAX_VALUE;
	private double maxX = Double.MIN_VALUE;
	private double minY = Double.MAX_VALUE;
	private double maxY = Double.MIN_VALUE;

	// --------------------- GETTER / SETTER METHODS ---------------------

	public double getMaxX()
		{
		return maxX;
		}

	public double getMinX()
		{
		return minX;
		}

	public double getMinY()
		{

		return minY;
		}

	public double getMaxY()
		{
		return maxY;
		}

	// -------------------------- OTHER METHODS --------------------------

	public void addPoint(double x, double y) throws StatsException
		{
		if (Double.isNaN(x) || Double.isInfinite(x))
			{
			throw new StatsException("Invalid x value in SimpleXYSeries: " + x);
			}
		if (Double.isNaN(y) || Double.isInfinite(y))
			{
			throw new StatsException("Invalid y value in SimpleXYSeries: " + y);
			}
		points.add(new XYPoint(x, y));
		if (x > maxX)
			{
			maxX = x;
			}
		if (y > maxY)
			{
			maxY = y;
			}
		if (x < minX)
			{
			minX = x;
			}
		if (y < minY)
			{
			minY = y;
			}
		}

	public double getX(int i)
		{
		return points.get(i).x;
		}

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

	public double getY(int i)
		{
		return points.get(i).y;
		}

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

	public double[] getYArray(double xmin, double xmax)
		{
		ArrayList<Double> result = new ArrayList<Double>();
		//	int i = 0;
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

	public Iterator getYiterator()
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

		// inefficient
		for (XYPoint p : points)
			{
			result.addPoint(p.x, DSArrayUtils.mean(getYArray(p.x - smoothFactor, p.x + smoothFactor)));
			}

		return result;
		}

	// -------------------------- INNER CLASSES --------------------------

	private static class XYPoint
		{
		public double x, y;

		public XYPoint(double x, double y)
			{
			this.x = x;
			this.y = y;
			}
		}
	}
