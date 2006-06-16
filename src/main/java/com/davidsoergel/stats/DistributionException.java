/*
 * dsutils - a collection of generally useful utility classes
 *
 * Copyright (c) 2001-2006 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * david@davidsoergel.com
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * USA.
 *
 */

package com.davidsoergel.stats;

import org.apache.log4j.Logger;
import com.davidsoergel.dsutils.ChainedException;

/**
 * @author lorax
 * @version 1.0
 */
public class DistributionException extends ChainedException
	{
// ------------------------------ FIELDS ------------------------------

	private static Logger logger = Logger.getLogger(DistributionException.class);

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
