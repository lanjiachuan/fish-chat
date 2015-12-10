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

import java.io.*;

/**
 * An on-the-wire representation of an MQTT SUBACK.
 */
public class MqttSuback extends MqttAck {

    /**
     * 
     */
    private static final long serialVersionUID = 4703827086974564978L;
    private int[] grantedQos; // Not currently made available to anyone. 

    public MqttSuback(MqttSubscribe sub) {
        super(MqttWireMessage.MESSAGE_TYPE_SUBACK);
        this.msgId = sub.getMessageId();
        this.grantedQos = sub.getQos();
    }
    
    public MqttSuback() {
        super(MqttWireMessage.MESSAGE_TYPE_SUBACK);
    }
    
    public MqttSuback(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_SUBACK);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();
        int index = 0;
        grantedQos = new int[data.length - 2];
        int qos = dis.read();
        while (qos != -1) {
            grantedQos[index] = qos;
            index++;
            qos = dis.read();
        }
        dis.close();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }

    /**
     * 
     * @param grantedQos subscribe消息中每个topic对应的qos值
     * @return
     * @throws MqttException
     */
    public byte[] getPayload(int[] grantedQos) throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            if (grantedQos != null && grantedQos.length > 0) {
                for (int i = 0; i < grantedQos.length; i++) {
                    dos.write(grantedQos[i]);
                }
            }
            dos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new MqttException(ex);
        }
    }

    public String toString() {
        StringBuilder rcBuilder = new StringBuilder(32);
        rcBuilder.append(super.toString()).append(" granted Qos");
//        String rc = super.toString() + " granted Qos";
        for (int i = 0; i < grantedQos.length; ++i) {
            rcBuilder.append(" ").append(grantedQos[i]);
//            rc += " " + grantedQos[i];
        }
        return rcBuilder.toString();
    }

}
