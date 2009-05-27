package com.davidsoergel.stats;

import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface RequiresPreparationDistanceMetric<T>
	{
	void prepare(Set<T> ids);

	boolean reallyRequiresPreparation();
	}
