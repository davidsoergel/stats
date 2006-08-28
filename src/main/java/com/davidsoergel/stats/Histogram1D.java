package com.davidsoergel.stats;

import org.apache.log4j.Logger;

// ** Redo using matrix library
public class Histogram1D //extends SimpleXYSeries
	{
	private static Logger logger = Logger.getLogger(Histogram1D.class);

	private int[] counts;
	double from, to, step;

	public Histogram1D(double from, double to, double step)
		{
		this.from = from;
		this.to = to;
		this.step = step;
		counts = new int[(int)(((to-from) / step) + 1)];
		}

	public void add(double x)
		{
		counts[bin(x)]++;
		}

	public int bin(double x)
		{
		return (int)((x-from) / step);
		}

	public int[] getCounts() { return counts; }

	public double centerOfBin(int i) { return from + (i * step) + (0.5 * step); }  
	}
