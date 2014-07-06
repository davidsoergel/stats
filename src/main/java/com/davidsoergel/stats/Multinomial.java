/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class Multinomial<T> implements Cloneable//extends HashMap<Double, T>
	{
	// ------------------------------ FIELDS ------------------------------

	private MultinomialDistribution dist = new MultinomialDistribution();
	//private List<T> elements = new ArrayList<T>();
	private BiMap<T, Integer> elementIndexes = HashBiMap.create(10);
	private int maxIndex = 0;

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

	public synchronized boolean isAlreadyNormalized() throws DistributionException
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

	public synchronized void put(@NotNull T obj, double prob) throws DistributionException//throws DistributionException
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

	public synchronized void normalize() throws DistributionException
		{
		dist.normalize();
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public synchronized Collection<T> getElements()
		{
		return elementIndexes.keySet();
		}

	// ------------------------ CANONICAL METHODS ------------------------

	public synchronized Multinomial<T> clone()
		{
		Multinomial<T> result = new Multinomial<T>();
		synchronized (result)
			{
			result.dist = new MultinomialDistribution(dist);
			result.elementIndexes = HashBiMap.create(elementIndexes);
			}
		return result;
		}

	// -------------------------- OTHER METHODS --------------------------

	public synchronized double KLDivergenceToThisFrom(Multinomial<T> belief) throws DistributionException
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

	public synchronized double getLog(T obj) throws DistributionException
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

	public synchronized double get(T obj) throws DistributionException//throws DistributionException
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

	public synchronized void mixIn(Multinomial<T> uniform, double smoothFactor) throws DistributionException
		{
		for (int c = 0; c < elementIndexes.size(); c++)
			{
			dist.probs[c] =
					(dist.probs[c] * (1. - smoothFactor)) + uniform.get(elementIndexes.inverse().get(c)) * smoothFactor;
			}
		}

	@NotNull
	public synchronized T sample() throws DistributionException
		{
		int index = dist.sample();
		T result = elementIndexes.inverse().get(index);
		if (result == null)
			{
			throw new Error("Impossible");
			}
		return result;
		}

	public synchronized int size()
		{
		return elementIndexes.size();
		}

	public synchronized void redistributeWithMinimum(double minimumProbability) throws DistributionException
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

	public synchronized double getDominantProbability()
		{
		return DSArrayUtils.max(dist.probs);
		}

	public synchronized T getDominantKey()
		{
		return elementIndexes.inverse().get(DSArrayUtils.argmax(dist.probs));
		}

	public synchronized void remove(T obj) throws DistributionException
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
	public synchronized void increment(T obj, double increment) throws DistributionException
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

	public synchronized Map<T, Double> getValueMap()
		{
		Map<T, Double> result = new HashMap<T, Double>();
		Set<Map.Entry<T, Integer>> entries = elementIndexes.entrySet();
		for (Map.Entry<T, Integer> entry : entries)
			{
			T key = entry.getKey();
			Integer value = entry.getValue();
			result.put(key, dist.probs[value]);
			}
		return result;
		}
	}
