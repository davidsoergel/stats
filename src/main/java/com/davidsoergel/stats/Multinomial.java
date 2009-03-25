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
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multiset;
import org.apache.commons.collections15.Bag;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class Multinomial<T> implements Cloneable//extends HashMap<Double, T>
	{
	// ------------------------------ FIELDS ------------------------------

	private MultinomialDistribution dist = new MultinomialDistribution();
	//private List<T> elements = new ArrayList<T>();
	private BiMap<T, Integer> elementIndexes = new HashBiMap<T, Integer>(10);
	int maxIndex = 0;

	// -------------------------- STATIC METHODS --------------------------

	public static <T> Multinomial<T> mixture(Multinomial<T> basis, Multinomial<T> bias, double mixingProportion)
			throws DistributionException
		{
		Multinomial<T> result = new Multinomial<T>();

		// these may be slow, remove later?
		assert basis.isAlreadyNormalized();
		assert bias.isAlreadyNormalized();
		assert basis.getElements().size() == bias.getElements().size();

		for (T key : basis.getElements())
			{
			double p = (1. - mixingProportion) * basis.get(key) + mixingProportion * bias.get(key);
			result.put(key, p);
			}
		assert result.isAlreadyNormalized();
		return result;
		}

	public boolean isAlreadyNormalized() throws DistributionException
		{
		return dist.isAlreadyNormalized();
		}

	// --------------------------- CONSTRUCTORS ---------------------------

	//	private Map<T, Double> logProbs = new HashMap<T, Double>();

	public Multinomial()
		{
		}

	public Multinomial(T[] keys, Map<T, Double> values) throws DistributionException
		{
		this();
		for (T k : keys)
			{
			put(k, values.get(k));
			}
		normalize();
		}

	public Multinomial(Bag<T> counts) throws DistributionException
		{
		this();
		for (T k : counts.uniqueSet())
			{
			put(k, counts.getCount(k));
			}
		normalize();
		}


	public Multinomial(Multiset<T> counts) throws DistributionException
		{
		this();
		for (Multiset.Entry<T> k : counts.entrySet())
			{
			put((T) k.getElement(), k.getCount());
			}
		normalize();
		}

	public void put(@NotNull T obj, double prob) throws DistributionException//throws DistributionException
		{
		if (elementIndexes.containsKey(obj))
			{
			dist.update(elementIndexes.get(obj), prob);
			//dist.normalize();
			//throw new DistributionException("Can't add the same element to a Multinomial twice");// don't bother to handle this properly
			}
		else
			{
			elementIndexes.put(obj, maxIndex);
			maxIndex++;
			dist.add(prob);
			//dist.normalize();
			}
		}

	public void normalize() throws DistributionException
		{
		dist.normalize();
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public Collection<T> getElements()
		{
		return elementIndexes.keySet();
		}

	// ------------------------ CANONICAL METHODS ------------------------

	public Multinomial<T> clone()
		{
		Multinomial<T> result = new Multinomial<T>();
		result.dist = new MultinomialDistribution(dist);
		result.elementIndexes = new HashBiMap<T, Integer>(elementIndexes);
		return result;
		}

	// -------------------------- OTHER METHODS --------------------------

	public double KLDivergenceToThisFrom(Multinomial<T> belief) throws DistributionException
		{
		double divergence = 0;
		for (T key : elementIndexes.keySet())
			{
			double p = get(key);
			double q = belief.get(key);
			if (p == 0 || q == 0)
				{
				throw new DistributionException("Can't compute KL divergence: distributions not smoothed");
				}

			divergence += p * MathUtils.approximateLog(p / q);

			if (Double.isNaN(divergence))
				{
				throw new DistributionException("Got NaN when computing KL divergence.");
				}
			}
		return divergence;
		}

	public double getLog(T obj) throws DistributionException
		{
		//		Double result = logProbs.get(obj);
		//		if (result == null)
		//			{
		//			result = MathUtils.approximateLog(get(obj));
		//			logProbs.put(obj, result);
		//			}
		//		return result;
		return MathUtils.approximateLog(get(obj));
		}

	public double get(T obj) throws DistributionException//throws DistributionException
		{
		Integer i = elementIndexes.get(obj);
		if (i == null)
			{
			//return 0;
			//return Double.NaN;
			throw new DistributionException("No probability known: " + obj);
			}
		return dist.probs[i];
		}

	public void mixIn(Multinomial<T> uniform, double smoothFactor) throws DistributionException
		{
		for (int c = 0; c < elementIndexes.size(); c++)
			{
			dist.probs[c] =
					(dist.probs[c] * (1. - smoothFactor)) + uniform.get(elementIndexes.inverse().get(c)) * smoothFactor;
			}
		}

	@NotNull
	public T sample() throws DistributionException
		{
		int index = dist.sample();
		T result = elementIndexes.inverse().get(index);
		if (result == null)
			{
			throw new Error("Impossible");
			}
		return result;
		}

	public int size()
		{
		return elementIndexes.size();
		}

	public void redistributeWithMinimum(double minimumProbability) throws DistributionException
		{
		double redistributionProportion = maxIndex * minimumProbability;
		if (redistributionProportion > 1.)
			{
			throw new DistributionException(
					"Can't use a minimum probability of " + minimumProbability + " for a multinomial with " + maxIndex
							+ "elements.");
			}
		for (int c = 0; c < maxIndex; c++)
			{
			dist.probs[c] = (1. - redistributionProportion) * dist.probs[c] + minimumProbability;
			}
		}

	public double getDominantProbability()
		{
		return DSArrayUtils.max(dist.probs);
		}

	public T getDominantKey()
		{
		return elementIndexes.inverse().get(DSArrayUtils.argmax(dist.probs));
		}

	public void remove(T obj) throws DistributionException
		{
		Integer i = elementIndexes.get(obj);
		if (i == null)
			{
			//return 0;
			//return Double.NaN;
			throw new DistributionException("Can't remove nonexistent element: " + obj);
			}
		elementIndexes.remove(obj);
		dist.probs = ArrayUtils.remove(dist.probs, i);
		dist.normalize();

		i++;
		while (i <= elementIndexes.size())
			{
			T t = elementIndexes.inverse().get(i);
			elementIndexes.put(t, i - 1);
			i++;
			}
/*
		for (Map.Entry<T, Integer> entry : elementIndexes.entrySet())
			{
			Integer v = entry.getValue();
			if (v > i)
				{
				entry.setValue(v - 1);
				}
			}
*/

		}

	/**
	 * Note this is entirely sensitive to the current normalization state.  The idea is that one might add a bunch of
	 * weights in a loop, and normalize afterwards.
	 *
	 * @param obj
	 * @param increment
	 * @throws DistributionException
	 */
	public void increment(T obj, double increment) throws DistributionException
		{
		try
			{
			double currentval = get(obj);
			dist.update(elementIndexes.get(obj), currentval + increment);
			}
		catch (DistributionException e)  // there wasn't already a value
			{
			elementIndexes.put(obj, maxIndex);
			maxIndex++;
			dist.add(increment);
			//dist.normalize();
			}
		}

	public Map<T, Double> getValueMap()
		{
		Map<T, Double> result = new HashMap<T, Double>();
		for (Map.Entry<T, Integer> entry : elementIndexes.entrySet())
			{
			result.put(entry.getKey(), dist.probs[entry.getValue()]);
			}
		return result;
		}
	}
