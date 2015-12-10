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
 * An on-the-wire representation of an MQTT UNSUBACK.
 */
public class MqttUnsubAck extends MqttAck {

    /**
     * 
     */
    private static final long serialVersionUID = 1637136142001884184L;

    public MqttUnsubAck(MqttUnsubscribe unsub) {
        super(MqttWireMessage.MESSAGE_TYPE_UNSUBACK);
        this.msgId = unsub.getMessageId();
    }
    
    public MqttUnsubAck() {
        super(MqttWireMessage.MESSAGE_TYPE_UNSUBACK);
    }
    
    public MqttUnsubAck(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_UNSUBACK);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();
        dis.close();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }
}
