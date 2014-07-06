/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.stats;

import com.davidsoergel.dsutils.ChainedException;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class StatsException extends ChainedException
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(StatsException.class);


	// --------------------------- CONSTRUCTORS ---------------------------

	public StatsException(Throwable e)
		{
		super(e);
		}

	public StatsException(String s)
		{
		super(s);
		}

	public StatsException(Throwable e, String s)
		{
		super(e, s);
		}
	}
