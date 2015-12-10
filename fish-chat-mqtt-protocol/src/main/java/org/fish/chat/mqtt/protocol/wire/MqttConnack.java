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
 * An on-the-wire representation of an MQTT CONNACK.
 */
public class MqttConnack extends MqttAck {

    /**
     * 
     */
    private static final long serialVersionUID = 5748155015961449698L;

    /**
     * 0 0x00 Connection Accepted
     */
    public static final byte ACCEPTED = 0x00;

    /**
     * 1 0x01 Connection Refused: unacceptable protocol version
     */
    public static final byte REFUSED_UNACCEPTABLE_PROTOCOL_VERSION = 0x01;

    /**
     * 2 0x02 Connection Refused: identifier rejected
     */
    public static final byte REFUSED_IDENTIFIER_REJECTED = 0x02;

    /**
     * 3 0x03 Connection Refused: server unavailable
     */
    public static final byte REFUSED_SERVER_UNAVAILABLE = 0x03;

    /**
     * 4 0x04 Connection Refused: bad user name or password
     */
    public static final byte REFUSED_BAD_USERNAME_PASSWORD = 0x04;

    /**
     * 5 0x05 Connection Refused: not authorized
     */
    public static final byte REFUSED_NOT_AUTHORIZED = 0x05;

    private byte[] variableHeader;

    private int returnCode;

    public MqttConnack() {
        super(MqttWireMessage.MESSAGE_TYPE_CONNACK);
        returnCode = ACCEPTED;
        variableHeader = new byte[] { 0x00, ACCEPTED };
    }

    public MqttConnack(byte info, byte[] variableHeader) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_CONNACK);
        ByteArrayInputStream bais = new ByteArrayInputStream(variableHeader);
        DataInputStream dis = new DataInputStream(bais);
        dis.readByte();
        returnCode = dis.readUnsignedByte();
        variableHeader = new byte[] { 0x00, (byte) returnCode };
        dis.close();
    }

    public MqttConnack(byte code) {
        super(MqttWireMessage.MESSAGE_TYPE_CONNACK);
        returnCode = code;
        variableHeader = new byte[] { 0x00, code };
    }

    public int getReturnCode() {
        return returnCode;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return variableHeader;
    }

    /**
     * Returns whether or not this message needs to include a message ID.
     */
    public boolean isMessageIdRequired() {
        return false;
    }

    public String getKey() {
        return "Con";
    }

    public String toString() {
        return super.toString() + " return code: " + returnCode;
    }
}
