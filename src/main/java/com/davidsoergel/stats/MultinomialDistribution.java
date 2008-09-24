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
import com.davidsoergel.dsutils.math.MathUtils;
import com.davidsoergel.dsutils.math.MersenneTwisterFast;

import java.util.List;

/**
 * @version $Id$
 */
public class MultinomialDistribution implements DiscreteDistribution1D
	{
	// ------------------------------ FIELDS ------------------------------

	static MersenneTwisterFast mtf = new MersenneTwisterFast();
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
		double r = mtf.nextDouble();//Math.random();
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
