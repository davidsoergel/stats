/*
 * Copyright (c) 2006-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.stats;

import org.apache.log4j.Logger;

/**
 * @version $Id$
 */
public class DistributionException extends StatsException
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(DistributionException.class);


	// --------------------------- CONSTRUCTORS ---------------------------

	public DistributionException(Throwable e)
		{
		super(e);
		}

	public DistributionException(String s)
		{
		super(s);
		}

	public DistributionException(Throwable e, String s)
		{
		super(e, s);
		}
	}
