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

import org.apache.log4j.Logger;
import org.fish.chat.mqtt.protocol.ExceptionHelper;
import org.fish.chat.mqtt.protocol.MqttException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An <code>MqttInputStream</code> lets applications read instances of
 * <code>MqttWireMessage</code>.
 */
public class MqttInputStream extends InputStream {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(MqttInputStream.class);

    private static final String className = MqttInputStream.class.getName();

    private DataInputStream in;

    public MqttInputStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public int read() throws IOException {
        return in.read();
    }

    public int available() throws IOException {
        return in.available();
    }

    public void close() throws IOException {
        in.close();
    }

    /**
     * Reads an <code>MqttWireMessage</code> from the stream.
     */
    public MqttWireMessage readMqttWireMessage() throws IOException, MqttException {
        final String methodName = "readMqttWireMessage";
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        byte first = in.readByte();
        byte type = (byte) ((first >>> 4) & 0x0F);
        if ((type < MqttWireMessage.MESSAGE_TYPE_CONNECT)
                || (type > MqttWireMessage.MESSAGE_TYPE_DISCONNECT)) {
            // Invalid MQTT message type...
            throw ExceptionHelper.createMqttException(MqttException.REASON_CODE_INVALID_MESSAGE);
        }
        long remLen = MqttWireMessage.readMBI(in).getValue();
        bais.write(first);
        // bit silly, we decode it then encode it
        bais.write(MqttWireMessage.encodeMBI(remLen));
        byte[] packet = new byte[(int) (bais.size() + remLen)];
        in.readFully(packet, bais.size(), packet.length - bais.size());
        byte[] header = bais.toByteArray();
        System.arraycopy(header, 0, packet, 0, header.length);
        MqttWireMessage message = MqttWireMessage.createWireMessage(packet);
        // @TRACE 501= received {0} 
        //        log.info(className, methodName, "501", new Object[] { message });
        return message;
    }
}
