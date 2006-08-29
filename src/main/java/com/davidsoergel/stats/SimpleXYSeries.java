package com.davidsoergel.stats;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * @author lorax
 * @version 1.0
 */
public class SimpleXYSeries
	{
	private static Logger logger = Logger.getLogger(SimpleXYSeries.class);

	private List<XYPoint> points = new ArrayList<XYPoint>();

	private double maxX;

	private class XYPoint
	{
	public double x, y;
	public XYPoint(double x, double y) { this.x = x; this.y = y; }
	}

	public void addOrUpdate(double x, double y)
		{
		points.add(new XYPoint(x, y));
		if(x > maxX) { maxX = x; }
		}

	public int size() { return points.size(); }
	public double getX(int i) { return points.get(i).x; }
	public double getY(int i) { return points.get(i).y; }

	public double getMaxX() { return maxX; }

	public double[] getXArray()
		{
		double[] result = new double[points.size()];
		int i = 0;
		for(XYPoint p: points)
			{
			result[i] = p.x;
			i++;
			}
		return result;
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
	}
