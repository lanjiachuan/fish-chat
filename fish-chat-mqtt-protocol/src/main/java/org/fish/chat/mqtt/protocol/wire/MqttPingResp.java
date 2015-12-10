/* 
 * Copyright (c) 2009, 2012 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Dave Locke - initial API and implementation and/or initial documentation
 */
package org.fish.chat.mqtt.protocol.wire;


import org.fish.chat.mqtt.protocol.MqttException;

/**
 * An on-the-wire representation of an MQTT PINGRESP.
 */
public class MqttPingResp extends MqttAck {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4689441692008273221L;

    public MqttPingResp() {
        super(MqttWireMessage.MESSAGE_TYPE_PINGRESP);
    }
    
	public MqttPingResp(byte info, byte[] variableHeader) {
		super(MqttWireMessage.MESSAGE_TYPE_PINGRESP);
	}
	//这个真没有
	protected byte[] getVariableHeader() throws MqttException {
		return new byte[0];
	}
	
	/**
	 * Returns whether or not this message needs to include a message ID.
	 */
	public boolean isMessageIdRequired() {
		return false;
	}
	
	public String getKey() {
		return "Pong";
	}
}
