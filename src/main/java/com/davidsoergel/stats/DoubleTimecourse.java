/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import java.util.ArrayList;

/**
 * @version $Id$
 */
public class DoubleTimecourse
	{
	// ------------------------------ FIELDS ------------------------------

	private String name;
	private double sum = 0;
	private ArrayList<Double> data = new ArrayList<Double>();
	private ArrayList<Double> runningAverage = new ArrayList<Double>();


	// --------------------------- CONSTRUCTORS ---------------------------

	public DoubleTimecourse(String name)
		{
		this.name = name;
		}

	// -------------------------- OTHER METHODS --------------------------

	public double last()
		{
		return data.get(data.size() - 1);
		}

	public String name()
		{
		return name;
		}

	public double runningaverage()
		{
		return runningAverage.get(runningAverage.size() - 1);
		}

	public void set(double d)
		{
		sum += d;
		data.add(d);
		runningAverage.add(sum / (double) data.size());
		}

	public ArrayList<Double> getData()
		{
		return data;
		}
	}
