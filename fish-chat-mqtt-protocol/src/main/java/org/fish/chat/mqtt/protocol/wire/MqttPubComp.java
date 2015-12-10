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
 * An on-the-wire representation of an MQTT PUBCOMP message.
 */
public class MqttPubComp extends MqttAck {

    /**
     * 
     */
    private static final long serialVersionUID = 5109883463840505007L;

    public MqttPubComp() {
        super(MqttWireMessage.MESSAGE_TYPE_PUBCOMP);
    }

    public MqttPubComp(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_PUBCOMP);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();
        dis.close();
    }

    public MqttPubComp(MqttPublish publish) {
        super(MqttWireMessage.MESSAGE_TYPE_PUBCOMP);
        this.msgId = publish.getMessageId();
    }

    public MqttPubComp(int msgId) {
        super(MqttWireMessage.MESSAGE_TYPE_PUBCOMP);
        this.msgId = msgId;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }
}
