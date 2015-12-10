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
import org.fish.chat.mqtt.protocol.MqttMessage;

import java.io.*;

/**
 * An on-the-wire representation of an MQTT CONNECT message.
 */
public class MqttConnect extends MqttWireMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -4924467460608940738L;

    public static String KEY = "Con";

    private String clientId;

    private boolean cleanSession;

    private MqttMessage willMessage;

    private String userName;

    private String password;

    private int keepAliveInterval;

    private String willDestination;

    private int protocolVersion;

    private String protocolName;

    private byte connectFlags;

    public MqttConnect() {
        super(MqttWireMessage.MESSAGE_TYPE_CONNECT);
    }
    
    /**
     * Constructor for an on the wire MQTT connect message
     * 
     * @param info
     * @param data
     * @throws java.io.IOException
     * @throws MqttException
     */
    public MqttConnect(byte info, byte[] data) throws IOException, MqttException {
        super(MqttWireMessage.MESSAGE_TYPE_CONNECT);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);

        protocolName = decodeUTF8(dis);
        protocolVersion = dis.readByte();
        connectFlags = dis.readByte();
        keepAliveInterval = dis.readUnsignedShort();
        clientId = decodeUTF8(dis);
        userName = decodeUTF8(dis);
        password = decodeUTF8(dis);
        dis.close();
    }

    public MqttConnect(String clientId, boolean cleanSession, int keepAliveInterval,
            String userName, String password, MqttMessage willMessage, String willDestination) {
        super(MqttWireMessage.MESSAGE_TYPE_CONNECT);
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.keepAliveInterval = keepAliveInterval;
        this.userName = userName;
        this.password = password;
        this.willMessage = willMessage;
        this.willDestination = willDestination;
    }

    public MqttConnect(String clientId, boolean cleanSession, int keepAliveInterval,
            String userName, String password) {
        super(MqttWireMessage.MESSAGE_TYPE_CONNECT);
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.keepAliveInterval = keepAliveInterval;
        this.userName = userName;
        this.password = password;
    }

    public String toString() {
        String rc = super.toString();
        rc += " clientId " + clientId + " keepAliveInterval " + keepAliveInterval;
        return rc;
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            encodeUTF8(dos, "MQIsdp");
            dos.write(3);
            byte connectFlags = 0;

            if (cleanSession) {
                connectFlags |= 0x02;
            }

            if (willMessage != null) {
                connectFlags |= 0x04;
                connectFlags |= (willMessage.getQos() << 3);
                if (willMessage.isRetained()) {
                    connectFlags |= 0x20;
                }
            }

            if (userName != null) {
                connectFlags |= 0x80;
                if (password != null) {
                    connectFlags |= 0x40;
                }
            }
            dos.write(connectFlags);
            dos.writeShort(keepAliveInterval);
            dos.flush();
            return baos.toByteArray();
        } catch (IOException ioe) {
            throw new MqttException(ioe);
        }
    }

    public byte[] getPayload() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            encodeUTF8(dos, clientId);

            if (willMessage != null) {
                encodeUTF8(dos, willDestination);
                dos.writeShort(willMessage.getPayload().length);
                dos.write(willMessage.getPayload());
            }

            if (userName != null) {
                encodeUTF8(dos, userName);
                if (password != null) {
                    encodeUTF8(dos, password);
                }
            }
            dos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new MqttException(ex);
        }
    }

    /**
     * Returns whether or not this message needs to include a message ID.
     */
    public boolean isMessageIdRequired() {
        return false;
    }

    public String getKey() {
        return KEY;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the willMessage
     */
    public MqttMessage getWillMessage() {
        return willMessage;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the keepAliveInterval
     */
    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    /**
     * @return the willDestination
     */
    public String getWillDestination() {
        return willDestination;
    }

    /**
     * @return the protocolVersion
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * @return the protocolName
     */
    public String getProtocolName() {
        return protocolName;
    }

    /**
     * @return the connectFlags
     */
    public byte getConnectFlags() {
        return connectFlags;
    }

}
