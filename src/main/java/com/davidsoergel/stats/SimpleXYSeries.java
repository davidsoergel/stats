package com.davidsoergel.stats;

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lorax
 * @version 1.0
 */
public class SimpleXYSeries
	{
	private static Logger logger = Logger.getLogger(SimpleXYSeries.class);

	private Set<XYPoint> points = new HashSet<XYPoint>();

	private class XYPoint
	{
	public double x, y;
	public XYPoint(double x, double y) { this.x = x; this.y = y; }
	}

	public void addOrUpdate(double x, double y)
		{
		points.add(new XYPoint(x, y));
		}
	}
