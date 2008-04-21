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

public class PearsonCorrelation
	{
	// -------------------------- STATIC METHODS --------------------------

	/*
	Pseodocade form Wikipedia
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
		double mean_x = x[1];
		double mean_y = y[1];
		int N = x.length;
		if (y.length != N)
			{
			throw new StatsException("Cannot compute correlation coefficient between arrays of different lengths");
			}
		for (int i = 2; i < N; i++)
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

		if (sum_sq_x == 0 || sum_sq_y == 0)
			{
			throw new StatsException("Can't compute Pearson correlation: distribution has no variance");
			}

		double pop_sd_x = Math.sqrt(sum_sq_x / N);
		double pop_sd_y = Math.sqrt(sum_sq_y / N);

		double cov_x_y = sum_coproduct / N;
		double correlation = cov_x_y / (pop_sd_x * pop_sd_y);
		if (Double.isNaN(correlation) || Double.isInfinite(correlation))
			{
			throw new StatsException("Error computing Pearson correlation: NaN or Infinity");
			}
		return correlation;
		}
	}
