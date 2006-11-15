package com.davidsoergel.stats;

import java.util.ArrayList;

/**
 * @author lorax
 * @version 1.0
 */
public class DoubleTimecourse
	{
	private String name;
	private double sum = 0;
	private ArrayList<Double> data = new ArrayList<Double>();
	private ArrayList<Double> runningAverage = new ArrayList<Double>();

	public DoubleTimecourse(String name)
		{
		this.name = name;
		}

	public void set(double d)
		{
		sum += d;
		data.add(d);
		runningAverage.add(sum / (double) data.size());
		}

	public double last()
		{
		return data.get(data.size() - 1);
		}

	public double runningaverage()
		{
		return runningAverage.get(runningAverage.size() - 1);
		}

	public String name()
		{
		return name;
		}
	}
