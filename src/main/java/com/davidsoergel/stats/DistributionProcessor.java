/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;


/**
 * An operator that performs some transformation on a distribution of the generic type.  Such a processor may smooth a
 * distribution, normalize it, etc.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public interface DistributionProcessor<T extends MutableDistribution>
	{
	// -------------------------- OTHER METHODS --------------------------
	/**
	 * Perform the operation represented by this processor on the given distribution, in place.
	 *
	 * @param distribution The distribution to process
	 * @throws DistributionProcessorException if anything goes wrong
	 */
	void process(T distribution) throws DistributionProcessorException;
	}
