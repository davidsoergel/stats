package com.davidsoergel.stats;

import com.davidsoergel.dsutils.DSArrayUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class ErrorBarXYSeries extends SimpleXYSeries
	{

	Map<Double, Set<Double>> yValsPerX = new HashMap<Double, Set<Double>>();

	@Override
	public void addPoint(double x, double y) throws StatsException
		{
		super.addPoint(x, y);
		getYSet(x).add(y);
		}

	private Set<Double> getYSet(double x)
		{
		Set<Double> result = yValsPerX.get(x);

		if (result == null)
			{
			result = new HashSet<Double>();
			yValsPerX.put(x, result);
			}
		return result;
		}


	// support error bars
	public Set<Double> uniqueXValues()
		{
		return yValsPerX.keySet();
		}

	public double meanYAtX(double x)
		{
		return DSArrayUtils.mean(getYSet(x));
		}

	public double stddevYAtX(double x, double mean)
		{

		return DSArrayUtils.stddev(getYSet(x), mean);
		}
	}
