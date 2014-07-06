/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

import com.davidsoergel.dsutils.math.MathUtils;

/**
 * This is the one-pass version.  See also LinearRegression, which also computes correlation.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class PearsonCorrelation
	{
	// -------------------------- STATIC METHODS --------------------------

	/*
	Pseudocode from Wikipedia
sum_sq_x = 0
sum_sq_y = 0
sum_coproduct = 0
mean_x = x[1]
mean_y = y[1]
for i in 2 to N:
    sweep = (i - 1.0) / i
    delta_x = x[i] - mean_x
    delta_y = y[i] - mean_y
    sum_sq_x += delta_x * delta_x * sweep
    sum_sq_y += delta_y * delta_y * sweep
    sum_coproduct += delta_x * delta_y * sweep
    mean_x += delta_x / i
    mean_y += delta_y / i
pop_sd_x = sqrt( sum_sq_x / N )
pop_sd_y = sqrt( sum_sq_y / N )
cov_x_y = sum_coproduct / N
correlation = cov_x_y / (pop_sd_x * pop_sd_y)
	 */


	public static double computeCorrelationCoefficient(DoubleArrayContainer x, DoubleArrayContainer y)
			throws StatsException
		{
		return computeCorrelationCoefficient(x.getArray(), y.getArray());
		}

	public static double computeCorrelationCoefficient(double[] x, double[] y) throws StatsException
		{
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = x[0];
		double mean_y = y[0];
		int n = x.length;
		if (y.length != n)
			{
			throw new StatsException("Cannot compute correlation coefficient between arrays of different lengths");
			}
		for (int i = 1; i < n; i++)
			{
			double sweep = (i - 1.0) / i;
			double delta_x = x[i] - mean_x;
			double delta_y = y[i] - mean_y;
			if (Double.isNaN(delta_x) || Double.isInfinite(delta_x))
				{
				throw new StatsException("Error computing Pearson correlation: NaN or Infinity");
				}
			if (Double.isNaN(delta_y) || Double.isInfinite(delta_y))
				{
				throw new StatsException("Error computing Pearson correlation: NaN or Infinity");
				}

			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
			}

		if (MathUtils.equalWithinFPError(sum_sq_x, 0) || MathUtils.equalWithinFPError(sum_sq_y, 0))
			{
			throw new StatsException("Can't compute Pearson correlation: distribution has no variance");
			//: \n\n" + Arrays.toString(x) + "\n\n" + Arrays.toString(y));  // temp huge error message
			}

		double pop_sd_x = Math.sqrt(sum_sq_x / n);
		double pop_sd_y = Math.sqrt(sum_sq_y / n);

		double cov_x_y = sum_coproduct / n;
		double correlation = cov_x_y / (pop_sd_x * pop_sd_y);
		if (Double.isNaN(correlation) || Double.isInfinite(correlation))
			{
			throw new StatsException("Error computing Pearson correlation: NaN or Infinity");
			}
		return correlation;
		}

	/**
	 * If either the x or y value at a given index equals the ignore value, drop that data point
	 *
	 * @param x
	 * @param y
	 * @param ignore
	 * @return
	 * @throws StatsException
	 */
	public static double computeCorrelationCoefficient(double[] x, double[] y, double ignore) throws StatsException
		{
		int n = x.length;
		if (y.length != n)
			{
			throw new StatsException("Cannot compute correlation coefficient between arrays of different lengths");
			}

		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;

		int startI = 0;
		while (x[startI] == ignore || y[startI] == ignore)
			{
			startI++;
			if (startI >= x.length)
				{
				throw new StatsException("All points are ignored");
				}
			}

		double mean_x = x[startI];
		double mean_y = y[startI];

		for (int i = startI + 1; i < n; i++)
			{
			if (x[i] == ignore || y[i] == ignore)
				{
				continue;
				}

			double sweep = (i - 1.0) / i;
			double delta_x = x[i] - mean_x;
			double delta_y = y[i] - mean_y;
			if (Double.isNaN(delta_x) || Double.isInfinite(delta_x))
				{
				throw new StatsException("Error computing Pearson correlation: NaN or Infinity");
				}
			if (Double.isNaN(delta_y) || Double.isInfinite(delta_y))
				{
				throw new StatsException("Error computing Pearson correlation: NaN or Infinity");
				}

			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
			}

		if (sum_sq_x == 0 || sum_sq_y == 0)
			{
			throw new StatsException("Can't compute Pearson correlation: distribution has no variance");
			//: \n\n" + Arrays.toString(x) + "\n\n" + Arrays.toString(y));  // temp huge error message
			}

		double pop_sd_x = Math.sqrt(sum_sq_x / n);
		double pop_sd_y = Math.sqrt(sum_sq_y / n);

		double cov_x_y = sum_coproduct / n;
		double correlation = cov_x_y / (pop_sd_x * pop_sd_y);
		if (Double.isNaN(correlation) || Double.isInfinite(correlation))
			{
			throw new StatsException("Error computing Pearson correlation: NaN or Infinity");
			}
		return correlation;
		}
	}
