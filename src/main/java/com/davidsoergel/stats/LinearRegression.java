package com.davidsoergel.stats;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class LinearRegression
	{
	public final double m;
	public final double b;

	public LinearRegression(double[] x, double[] y) throws StatsException
		{
		int n = x.length;
		if (y.length != n)
			{
			throw new StatsException("Cannot compute linear regression between arrays of different lengths");
			}

		double sumX = 0, sumXY = 0, sumXSquared = 0, sumY = 0;

		for (int i = 0; i < n; i++)
			{
			sumX += x[i];
			sumXSquared += x[i] * x[i];
			sumY += y[i];
			sumXY += x[i] * y[i];
			}


		m = (sumX * sumY - n * sumXY) / (sumX * sumX - n * sumXSquared);

		b = (sumX * sumXY - sumY * sumXSquared) / (sumX * sumX - n * sumXSquared);

		if (Double.isNaN(m) || Double.isInfinite(m) || Double.isNaN(b) || Double.isInfinite(b))
			{
			throw new StatsException("Regression failed with NaN or infinity");
			}
		}
	}
