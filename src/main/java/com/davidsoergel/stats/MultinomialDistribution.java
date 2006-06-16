/*
 * dsutils - a collection of generally useful utility classes
 *
 * Copyright (c) 2001-2006 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * david@davidsoergel.com
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * USA.
 *
 */

package com.davidsoergel.stats;

import com.davidsoergel.stats.DiscreteDistribution1D;
import com.davidsoergel.stats.DistributionException;
import com.davidsoergel.dsutils.ArrayUtils;

/**
 * @author lorax
 * @version 1.0
 */
public class MultinomialDistribution implements DiscreteDistribution1D
	{
// ------------------------------ FIELDS ------------------------------

	double[] probs = new double[0];
	private boolean isNormalized = false;

// --------------------------- CONSTRUCTORS ---------------------------

	public MultinomialDistribution() {}

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

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface DiscreteDistribution1D ---------------------

	public int sample() throws DistributionException
		{
		if (!isNormalized)
			{
			throw new DistributionException("Multinomial distribution is not normalized.");
			}
		double r = Math.random();
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
	}
