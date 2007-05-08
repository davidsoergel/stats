package com.davidsoergel.stats;

import org.apache.log4j.Logger;

import java.util.Set;

// ** Redo using matrix library
public class Histogram1D//extends SimpleXYSeries
	{
	private static Logger logger = Logger.getLogger(Histogram1D.class);

	private int[] counts;
	int validcounts, totalcounts;
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

	private double sum = 0;

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
			validcounts++;
			}
		sum += x;
		totalcounts++;
		}

	public void addXValues(SimpleXYSeries s)
		{
		for (double i : s.getXArray())
			{
			add(i);
			}
		}

	public void addYValues(SimpleXYSeries s)
		{
		for (double i : s.getYArray())
			{
			add(i);
			}
		}

	public void addXValues(Set<SimpleXYSeries> ss)
		{
		for (SimpleXYSeries s : ss)
			{
			addXValues(s);
			}
		}

	public void addYValues(Set<SimpleXYSeries> ss)
		{
		for (SimpleXYSeries s : ss)
			{
			addYValues(s);
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

	//private double sum = 0;
	/*	public double approximateMean()
		 {
		 //int i = 0;
		 if(mean == 0)
			 {

		 for (int i = 0; i < counts.length; i++)
			 {
			 mean += counts[i] * centerOfBin(i);
			 }
		 mean /= validcounts;
			 }
		 return mean;
		 }
 */

	public double mean()
		{
		return sum / totalcounts;
		}

	public double approximateStdDev()
		{
		double mean = mean();
		double var = 0;
		for (int i = 0; i < counts.length; i++)
			{
			double d = centerOfBin(i) - mean;
			var += counts[i] * d * d;
			}
		var /= validcounts;
		return Math.sqrt(var);
		}
	}
