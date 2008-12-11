package com.davidsoergel.stats;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface ProbabilisticDissimilarityMeasure<T> extends DissimilarityMeasure<T>
	{
	/**
	 * Some dissimilarity measures are probabilistic in nature, so a prior can be applied to them.  For instance, a log
	 * probability can be modified by adding the log prior.
	 *
	 * @param priorProbability
	 * @return
	 */

	double distanceFromTo(T a, T b, double priorProbability);

	//	double applyPrior(double rawDistance, double priorProbability);
	}
