/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;
import com.davidsoergel.dsutils.math.MathUtils;
import com.davidsoergel.dsutils.math.MersenneTwisterFast;

import java.util.List;

/**
 * @version $Id$
 */
public class MultinomialDistribution implements DiscreteDistribution1D
	{
	// ------------------------------ FIELDS ------------------------------

	//private static MersenneTwisterFast mtf = new MersenneTwisterFast();
	double[] probs = new double[0];
	private boolean normalized = false;


	// --------------------------- CONSTRUCTORS ---------------------------

	public MultinomialDistribution()
		{
		}

	public MultinomialDistribution(MultinomialDistribution copyFrom)
		{
		probs = copyFrom.probs.clone();
		normalized = copyFrom.normalized;
		}

	public MultinomialDistribution(double[] p) throws DistributionException
		{
		probs = new double[p.length];
		System.arraycopy(p, 0, probs, 0, p.length);
		normalize();
		}

	public MultinomialDistribution(List<Double> doubles) throws DistributionException
		{
		this(DSArrayUtils.toPrimitive(doubles.toArray(DSArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY)));
		}

	public void normalize() throws DistributionException
		{
		double normalizer = 0;
		for (int i = 0; i < probs.length; i++)
			{
			if (probs[i] < 0)
				{
				throw new DistributionException("Negative probability!");
				}
			normalizer += probs[i];
			}
		if (probs.length != 0 && normalizer <= 0)
			{
			throw new DistributionException("Can't normalize; no probability weight!");
			}
		for (int i = 0; i < probs.length; i++)
			{
			probs[i] /= normalizer;
			}
		normalized = true;
		}

	/**
	 * Constructs a new MultinomialDistribution by
	 *
	 * @param p the int[]
	 */
	public MultinomialDistribution(int[] p) throws DistributionException
		{
		probs = DSArrayUtils.castToDouble(p);
		//System.arraycopy(p, 0, probs, 0, p.length);
		normalize();
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface DiscreteDistribution1D ---------------------

	public int sample() throws DistributionException
		{
		if (!normalized)
			{
			//normalize();
			throw new DistributionException(
					"Multinomial distribution is not normalized.");// force the programmer to pay attention
			}
		double r = MersenneTwisterFast.random();
		int c = 0;
		while (r >= probs[c])
			{
			r -= probs[c];
			c++;
			}
		return c;
		}

	// -------------------------- OTHER METHODS --------------------------

	public void add(double prob) throws DistributionException
		{
		if (prob < 0)
			{
			throw new DistributionException("Negative probability!");
			}
		probs = DSArrayUtils.grow(probs, 1);
		probs[probs.length - 1] = prob;
		normalized = false;
		}

	/**
	 * Check whether the distribution is already normalized, even if it hasn't been explicitly normalized. Useful for
	 * testing whether a freshly constructed distribution is valid.
	 *
	 * @return
	 * @throws DistributionException
	 */
	public boolean isAlreadyNormalized() throws DistributionException
		{
		if (!normalized)
			{
			double sum = 0;
			for (int i = 0; i < probs.length; i++)
				{
				if (probs[i] < 0)
					{
					throw new DistributionException("Negative probability!");
					}
				sum += probs[i];
				}
			if (MathUtils.equalWithinFPError(sum, 1.0))
				{
				normalized = true;
				}
			}
		return normalized;
		}

	public void update(int index, double prob) throws DistributionException
		{
		if (prob < 0)
			{
			throw new DistributionException("Negative probability!");
			}
		probs[index] = prob;
		normalized = false;
		}

	public double[] getProbs()
		{
		return probs;
		}
	}
