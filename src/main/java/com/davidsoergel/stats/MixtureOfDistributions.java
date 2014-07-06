/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Models a mixture of distributions in a multidimensional linear space.
 *
 * @version $Id$
 */
public class MixtureOfDistributions
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(MixtureOfDistributions.class);

	int d;
	MultinomialDistribution mixture = new MultinomialDistribution();
	List<ContinuousDistribution> theComponents = new ArrayList<ContinuousDistribution>();


	// --------------------------- CONSTRUCTORS ---------------------------

	public MixtureOfDistributions(int d)
		{
		this.d = d;
		}

	// -------------------------- OTHER METHODS --------------------------

	public void add(ContinuousDistribution c, double proportion) throws DistributionException
		{
		if (c.getDimensionality() != d)
			{
			throw new DistributionException("Can't add distribution to a mixture of different dimensionality");
			}
		theComponents.add(c);
		mixture.add(proportion);
		}

	public void normalize() throws DistributionException
		{
		mixture.normalize();
		}

	public double[] sample() throws DistributionException
		{
		int classid = mixture.sample();

		return theComponents.get(classid).sample();
		}
	}
