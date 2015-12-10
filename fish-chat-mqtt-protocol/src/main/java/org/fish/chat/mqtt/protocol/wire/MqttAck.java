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

/**
 * Abstract super-class of all acknowledgement messages.
 */
public abstract class MqttAck extends MqttWireMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 6108670566069767706L;

    public MqttAck(byte type) {
        super(type);
    }

    protected byte getMessageInfo() {
        return 0;
    }

    /**
     * @return String representation of the wire message
     */
    public String toString() {
        return super.toString() + " msgId " + msgId;
    }
}
