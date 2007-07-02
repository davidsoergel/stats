/* $Id$ */

/*
 * Copyright (c) 2001-2007 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * david@davidsoergel.com
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: lorax Date: Jun 24, 2007 Time: 10:36:27 PM To change this template use File |
 * Settings | File Templates.
 */
public class Multinomial<T>//extends HashMap<Double, T>
	{
	// ------------------------------ FIELDS ------------------------------

	MultinomialDistribution dist = new MultinomialDistribution();
	List<T> elements = new ArrayList<T>();


	// -------------------------- OTHER METHODS --------------------------

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
