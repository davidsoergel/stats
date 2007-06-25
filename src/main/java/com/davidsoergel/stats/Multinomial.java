package com.davidsoergel.stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: lorax Date: Jun 24, 2007 Time: 10:36:27 PM To change this template use File |
 * Settings | File Templates.
 */
public class Multinomial<T>//extends HashMap<Double, T>
	{
	MultinomialDistribution dist = new MultinomialDistribution();
	List<T> elements = new ArrayList<T>();

	public void put(T obj, double prob) throws DistributionException
		{
		if (elements.contains(obj))
			{
			dist.update(elements.indexOf(obj), prob);
			dist.normalize();
			//throw new DistributionException("Can't add the same element to a Multinomial twice");// don't bother to handle this properly
			}
		else
			{
			elements.add(obj);
			dist.add(prob);
			dist.normalize();
			}
		}

	public T sample() throws DistributionException
		{
		return elements.get(dist.sample());
		}
	}
