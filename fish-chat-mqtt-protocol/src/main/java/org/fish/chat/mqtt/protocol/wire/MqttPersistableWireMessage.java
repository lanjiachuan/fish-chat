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
import org.fish.chat.mqtt.protocol.MqttPersistable;
import org.fish.chat.mqtt.protocol.MqttPersistenceException;

public abstract class MqttPersistableWireMessage extends MqttWireMessage implements MqttPersistable {

    private int sendTimes = 1;

    public MqttPersistableWireMessage(byte type) {
        super(type);
    }

    public byte[] getHeaderBytes() throws MqttPersistenceException {
        try {
            return getHeader();
        } catch (MqttException ex) {
            throw new MqttPersistenceException(ex.getCause());
        }
    }

    public int getHeaderLength() throws MqttPersistenceException {
        return getHeaderBytes().length;
    }

    public int getHeaderOffset() throws MqttPersistenceException {
        return 0;
    }

    //	public String getKey() throws MqttPersistenceException {
    //		return new Integer(getMessageId()).toString();
    //	}

    public byte[] getPayloadBytes() throws MqttPersistenceException {
        try {
            return getPayload();
        } catch (MqttException ex) {
            throw new MqttPersistenceException(ex.getCause());
        }
    }

    public int getPayloadLength() throws MqttPersistenceException {
        return 0;
    }

    public int getPayloadOffset() throws MqttPersistenceException {
        return 0;
    }

    /**
     * @return the sendTimes
     */
    public int getSendTimes() {
        return sendTimes;
    }

    /**
     * @param sendTimes the sendTimes to set
     */
    public void setSendTimes(int sendTimes) {
        this.sendTimes = sendTimes;
    }

}
