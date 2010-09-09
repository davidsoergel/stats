package com.davidsoergel.stats;

import com.davidsoergel.dsutils.math.MersenneTwisterFast;
import com.google.common.base.Function;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Given a scatterplot and an x value, sample from the distribution of Y values in a column of a given width above the
 * given x.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class KernelDensityFunctionSampler implements Serializable, Function<Double, Double>
	{
	private double[] Xs;
	private double[] Ys;

	//double binwidth;

	// for now we'll determine the bin width just from a number of points
	private final int pointsWidth;
	private final int halfPointsWidth;
	private final int maxLeft;

	public KernelDensityFunctionSampler(SimpleXYSeries data, int pointsWidth)
		{
		data.sortByX();
		Xs = data.getXArray();
		Ys = data.getYArray();
		this.pointsWidth = pointsWidth;
		this.halfPointsWidth = pointsWidth / 2;
		maxLeft = Xs.length - pointsWidth;
		}

	/**
	 * The scatterplot must be sorted along the X axis!
	 *
	 * @param xs
	 * @param ys
	 * @param pointsWidth
	 */
	public KernelDensityFunctionSampler(final double[] xs, final double[] ys, int pointsWidth)
		{
		Xs = xs;
		Ys = ys;
		this.pointsWidth = pointsWidth;
		this.halfPointsWidth = pointsWidth / 2;

		maxLeft = xs.length - pointsWidth;
		}

	public Double apply(Double x)

		{
		int center = Arrays.binarySearch(Xs, x);

		center = center < 0 ? -(center + 1) : center;
		// if the original center = -insertionPoint-1, then now it's insertionPoint


		// if a bunch of x values are the same (as often happens with 0) the binary search always picks the same one,
		// but we need to randomize to get the edge effects right.

		// PERF these searches could be done by nested binary search

		// the block indexes start out exclusive
		int leftEqualBlock = center - 1;
		try
			{
			while (Xs[leftEqualBlock] == x)
				{
				leftEqualBlock--;
				}
			}
		catch (ArrayIndexOutOfBoundsException e)
			{
			leftEqualBlock = -1;
			}

		int rightEqualBlock = center + 1;
		try
			{
			while (Xs[rightEqualBlock] == x)
				{
				rightEqualBlock++;
				}
			}
		catch (ArrayIndexOutOfBoundsException e)
			{
			rightEqualBlock = Xs.length;
			}

		// now make them inclusive
		leftEqualBlock++;
		rightEqualBlock--;

		if (leftEqualBlock != rightEqualBlock)
			{
			center = leftEqualBlock + MersenneTwisterFast.randomInt(1 + rightEqualBlock - leftEqualBlock);
			}

		int left = center - halfPointsWidth;
		left = Math.max(left, 0);
		left = Math.min(left, maxLeft);

		int sampleIndex = left + MersenneTwisterFast.randomInt(pointsWidth);

		return Ys[sampleIndex];
		}
	}
