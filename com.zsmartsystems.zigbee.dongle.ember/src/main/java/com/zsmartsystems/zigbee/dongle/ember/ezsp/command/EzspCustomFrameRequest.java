/**
 * Copyright (c) 2016-2020 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.zigbee.dongle.ember.ezsp.command;

import com.zsmartsystems.zigbee.dongle.ember.ezsp.EzspFrameRequest;
import com.zsmartsystems.zigbee.dongle.ember.internal.serializer.EzspSerializer;

/**
 * Class to implement the Ember EZSP command <b>customFrame</b>.
 * <p>
 * Provides the customer a custom EZSP frame. On the NCP, these frames are only handled if the
 * XNCP library is included. On the NCP side these frames are handled in the
 * emberXNcpIncomingCustomEzspMessageCallback() callback function.
 * <p>
 * This class provides methods for processing EZSP commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class EzspCustomFrameRequest extends EzspFrameRequest {
    public static final int FRAME_ID = 0x47;

    /**
     * The length of the custom frame payload (maximum 119 bytes).
     * <p>
     * EZSP type is <i>uint8_t</i> - Java type is {@link int}
     */
    private int payloadLength;

    /**
     * The payload of the custom frame.
     * <p>
     * EZSP type is <i>uint8_t[]</i> - Java type is {@link int[]}
     */
    private int[] payload;

    /**
     * Serialiser used to serialise to binary line data
     */
    private EzspSerializer serializer;

    /**
     * Request constructor
     */
    public EzspCustomFrameRequest() {
        frameId = FRAME_ID;
        serializer = new EzspSerializer();
    }

    /**
     * The length of the custom frame payload (maximum 119 bytes).
     * <p>
     * EZSP type is <i>uint8_t</i> - Java type is {@link int}
     *
     * @return the current payloadLength as {@link int}
     */
    public int getPayloadLength() {
        return payloadLength;
    }

    /**
     * The length of the custom frame payload (maximum 119 bytes).
     *
     * @param payloadLength the payloadLength to set as {@link int}
     */
    public void setPayloadLength(int payloadLength) {
        this.payloadLength = payloadLength;
    }

    /**
     * The payload of the custom frame.
     * <p>
     * EZSP type is <i>uint8_t[]</i> - Java type is {@link int[]}
     *
     * @return the current payload as {@link int[]}
     */
    public int[] getPayload() {
        return payload;
    }

    /**
     * The payload of the custom frame.
     *
     * @param payload the payload to set as {@link int[]}
     */
    public void setPayload(int[] payload) {
        this.payload = payload;
    }

    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(serializer);

        // Serialize the fields
        serializer.serializeUInt8(payloadLength);
        serializer.serializeUInt8Array(payload);
        return serializer.getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(75);
        builder.append("EzspCustomFrameRequest [payloadLength=");
        builder.append(payloadLength);
        builder.append(", payload=");
        for (int cnt = 0; cnt < payload.length; cnt++) {
            if (cnt > 0) {
                builder.append(' ');
            }
            builder.append(payload[cnt]);
        }
        builder.append(']');
        return builder.toString();
    }
}
