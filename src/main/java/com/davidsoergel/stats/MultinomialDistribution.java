/* $Id$ */

/*
 * Copyright (c) 2001-2007 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * david@davidsoergel.com
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

import com.davidsoergel.dsutils.ArrayUtils;
import com.davidsoergel.dsutils.MersenneTwisterFast;

/**
 * @author lorax
 * @version 1.0
 */
public class MultinomialDistribution implements DiscreteDistribution1D
	{
	// ------------------------------ FIELDS ------------------------------

	static MersenneTwisterFast mtf = new MersenneTwisterFast();
	double[] probs = new double[0];
	private boolean isNormalized = false;


	// --------------------------- CONSTRUCTORS ---------------------------

	public MultinomialDistribution()
		{
		}

	public MultinomialDistribution(double[] p)
		{
		probs = new double[p.length];
		System.arraycopy(p, 0, probs, 0, p.length);
		normalize();
		}

	public void normalize()
		{
		double normalizer = 0;
		for (int i = 0; i < probs.length; i++)
			{
			normalizer += probs[i];
			}
		for (int i = 0; i < probs.length; i++)
			{
			probs[i] /= normalizer;
			}
		isNormalized = true;
		}

	public MultinomialDistribution(int[] p)
		{
		probs = ArrayUtils.castToDouble(p);
		//System.arraycopy(p, 0, probs, 0, p.length);
		normalize();
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface DiscreteDistribution1D ---------------------

	public int sample() throws DistributionException
		{
		if (!isNormalized)
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

	public void add(double prob)
		{
		probs = ArrayUtils.grow(probs, 1);
		probs[probs.length - 1] = prob;
		isNormalized = false;
		}

	public void update(int index, double prob)
		{
		probs[index] = prob;
		isNormalized = false;
		}
	}
