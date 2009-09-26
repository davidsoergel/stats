package com.davidsoergel.stats;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class LinearRegression
	{
	public final double slope;
	public final double intercept;
	public final double R2;

	public int n;
	private final double slopeStdErr;
	private final double interceptStdErr;
	private final double interceptStdErr2;

	public LinearRegression(double[] x, double[] y) throws StatsException
		{
		n = x.length;
		if (y.length != n)
			{
			throw new StatsException("Cannot compute linear regression between arrays of different lengths");
			}

		/* WTF is this?
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
		*/

		// this version from http://www.cs.princeton.edu/introcs/97data/LinearRegression.java.html

		// first pass: read in data, compute xbar and ybar
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
		for (int i = 0; i < n; i++)
			{
			sumx += x[i];
			sumx2 += x[i] * x[i];
			sumy += y[i];
			}
		double xbar = sumx / n;
		double ybar = sumy / n;

		// second pass: compute summary statistics
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = 0; i < n; i++)
			{
			xxbar += (x[i] - xbar) * (x[i] - xbar);
			yybar += (y[i] - ybar) * (y[i] - ybar);
			xybar += (x[i] - xbar) * (y[i] - ybar);
			}
		slope = xybar / xxbar;
		intercept = ybar - slope * xbar;

		if (Double.isNaN(this.slope) || Double.isInfinite(this.slope) || Double.isNaN(this.intercept) || Double
				.isInfinite(this.intercept))
			{
			throw new StatsException("Regression failed with NaN or infinity");
			}


		// print results
		//	System.out.println("y   = " + beta1 + " * x + " + beta0);

		// analyze results
		int df = n - 2;
		double rss = 0.0;      // residual sum of squares
		double ssr = 0.0;      // regression sum of squares

		// third pass: correlation etc.
		for (int i = 0; i < n; i++)
			{
			double fit = slope * x[i] + intercept;
			rss += (fit - y[i]) * (fit - y[i]);
			ssr += (fit - ybar) * (fit - ybar);
			}
		R2 = ssr / yybar;
		double svar = rss / df;
		double svar1 = svar / xxbar;
		double svar0 = svar / n + xbar * xbar * svar1;

		slopeStdErr = Math.sqrt(svar1);
		interceptStdErr = Math.sqrt(svar0);
		svar0 = svar * sumx2 / (n * xxbar);
		interceptStdErr2 = Math.sqrt(svar0);

		/*
		  System.out.println("R^2                 = " + R2);
		  System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
		  System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
		  svar0 = svar * sumx2 / (n * xxbar);
		  System.out.println("std error of beta_0 = " + Math.sqrt(svar0));

		  System.out.println("SSTO = " + yybar);
		  System.out.println("SSE  = " + rss);
		  System.out.println("SSR  = " + ssr);
  */


		}
	}
