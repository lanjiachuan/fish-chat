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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * An on-the-wire representation of an MQTT PUBREL message.
 */
public class MqttPubRel extends MqttPersistableWireMessage {

	/**
     * 
     */
    private static final long serialVersionUID = -3816722136985729648L;

    public MqttPubRel() {
        super(MqttWireMessage.MESSAGE_TYPE_PUBREL);
    }
    
    /**
	 * Createa a pubrel message based on a pubrec
	 * @param pubRec
	 */
	public MqttPubRel(MqttPubRec pubRec) {
		super(MqttWireMessage.MESSAGE_TYPE_PUBREL);
		this.setMessageId(pubRec.getMessageId());
	}
	
	/**
	 * Creates a pubrel based on a pubrel set of bytes read fro the network
	 * @param info
	 * @param data
	 * @throws java.io.IOException
	 */
	public MqttPubRel(byte info, byte[] data) throws IOException {
		super(MqttWireMessage.MESSAGE_TYPE_PUBREL);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
		msgId = dis.readUnsignedShort();
		dis.close();
	}

	protected byte[] getVariableHeader() throws MqttException {
		return encodeMessageId();
	}
	
	protected byte getMessageInfo() {
		return (byte)( 2 | (this.duplicate?8:0));
	}

	public String toString() {
    	return super.toString() + " msgId " + msgId;
	}

}
