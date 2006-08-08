package com.davidsoergel.stats;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Models a mixture of distributions in a multidimensional linear space.
 *
 * @author lorax
 * @version 1.0
 */
public class MixtureOfDistributions
	{
	private static Logger logger = Logger.getLogger(MixtureOfDistributions.class);

	int d;
	MultinomialDistribution mixture = new MultinomialDistribution();
	List<ContinuousDistribution> theComponents = new ArrayList<ContinuousDistribution>();

	public MixtureOfDistributions(int d)
		{
		this.d = d;
		}

	public double[] sample() throws DistributionException
		{
		int classid = mixture.sample();

		return theComponents.get(classid).sample();
		}

	public void add(ContinuousDistribution c, double proportion) throws DistributionException
		{
		if (c.getDimensionality() != d)
			{
			throw new DistributionException("Can't add distribution to a mixture of different dimensionality");
			}
		theComponents.add(c);
		mixture.add(proportion);
		}

	public void normalize()
		{
		mixture.normalize();
		}

	}