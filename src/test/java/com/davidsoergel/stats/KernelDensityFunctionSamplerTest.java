package com.davidsoergel.stats;

import junit.framework.TestCase;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class KernelDensityFunctionSamplerTest extends TestCase
	{
	@Test
	public void exercise() throws StatsException
		{
		SimpleXYSeries data = new SimpleXYSeries();
		data.addPoint(1, 1);
		data.addPoint(2, 2);
		data.addPoint(3, 3);
		data.addPoint(4, 4);
		data.addPoint(5, 5);
		data.addPoint(6, 6);
		data.addPoint(7, 7);
		data.addPoint(8, 8);
		data.addPoint(9, 9);
		data.addPoint(10, 10);

		KernelDensityFunctionSampler d = new KernelDensityFunctionSampler(data, 3);

		for (int i = 0; i < 10; i++)
			{
			double s = d.apply(5.);
			assert s >= 4 && s <= 6;
			}
		}
	}
