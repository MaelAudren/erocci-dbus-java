/*******************************************************************************
 *
 * OCCIware MART: OCCI Infrastructure Dummy Connector
 *
 * Copyright (c) 2016 Inria
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  - Philippe Merle <philippe.merle@inria.fr>
 *
 *******************************************************************************/
package org.ow2.mart.connector.infrastructure.dummy;

// TODO: Uncomment following line related to issue https://github.com/occiware/ecore/issues/26
// import org.occiware.clouddesigner.occi.infrastructure.NetworkStatus;
import org.occiware.clouddesigner.occi.infrastructure.ComputeStatus;

/**
 * This class is a dummy implementation of the OCCI Infrastructure Network kind.
 *
 * A skeleton of this class can be generated automatically.
 * See issue https://github.com/occiware/ecore/issues/54.
 *
 * @author philippe.merle@inria.fr
 */
public class NetworkConnector 
     extends org.occiware.clouddesigner.occi.infrastructure.impl.NetworkImpl
{
	/**
	 * Constructs a network connector.
	 */
	NetworkConnector()
	{
		System.err.println("DEBUG constructor " + this);
	}

	@Override
    public void up()
	{
		System.err.println("DEBUG up " + this);

		// TODO: Implement how to up this network.

// TODO: Uncomment following line related to issue https://github.com/occiware/ecore/issues/26
//		setState(NetworkStatus.ACTIVE);
		setState(ComputeStatus.ACTIVE);
	}

	@Override
    public void down()
	{
		System.err.println("DEBUG down " + this);

		// TODO: Implement how to down this network.

// TODO: Uncomment following line  related to issue https://github.com/occiware/ecore/issues/26
//		setState(NetworkStatus.INACTIVE);
		setState(ComputeStatus.INACTIVE);
	}
}
