package com.davidsoergel.stats;

import org.apache.log4j.Logger;

import java.util.Set;

// ** Redo using matrix library
public class Histogram1D//extends SimpleXYSeries
	{
	private static Logger logger = Logger.getLogger(Histogram1D.class);

	private int[] counts;
	int totalcounts;
	private int maxbin;
	private int underflow, overflow;
	private double from, to, binwidth;

	public Histogram1D(double from, double to, double binwidth)
		{
		this.from = from;
		this.to = to;
		this.binwidth = binwidth;
		maxbin = (int) ((to - from) / binwidth);
		counts = new int[maxbin + 1];
		}

	public Histogram1D(double from, double to, double step, double[] data)
		{
		this.from = from;
		this.to = to;
		this.binwidth = step;
		counts = new int[(int) (((to - from) / step) + 1)];
		for (double d : data)
			{
			add(d);
			}
		}

	public void add(double x)
		{
		int b = bin(x);
		if (b < 0)
			{
			underflow++;
			}
		else if (b > maxbin)
			{
			overflow++;
			}
		else
			{
			counts[bin(x)]++;
			totalcounts++;
			}
		}

	public void add(SimpleXYSeries s)
		{
		for (double i : s.getXArray())
			{
			add(i);
			}
		}

	public void add(Set<SimpleXYSeries> ss)
		{
		for (SimpleXYSeries s : ss)
			{
			add(s);
			}
		}

	public int bin(double x)
		{
		return (int) ((x - from) / binwidth);
		}

	public int[] getCounts()
		{
		return counts;
		}

	public double centerOfBin(int i)
		{
		return from + (i * binwidth) + (0.5 * binwidth);
		}

	public double approximateMean()
		{
		//int i = 0;
		double mean = 0;
		for (int i = 0; i < counts.length; i++)
			{
			mean += counts[i] * centerOfBin(i);
			}
		mean /= totalcounts;
		return mean;
		}

	public double approximateStdDev()
		{
		double mean = approximateMean();
		for (int i = 0; i < counts.length; i++)
			{
			mean += counts[i] * centerOfBin(i);
			}
		mean /= totalcounts;
		return mean;
		}
	}
