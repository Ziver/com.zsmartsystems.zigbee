/**
 * Copyright (c) 2016-2018 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.zigbee.dongle.ember.ezsp.command;

import com.zsmartsystems.zigbee.dongle.ember.ezsp.EzspFrameResponse;

/**
 * Class to implement the Ember EZSP command <b>energyScanResultHandler</b>.
 * <p>
 * Reports the result of an energy scan for a single channel. The scan is not complete until the
 * scanCompleteHandler callback is called.
 * <p>
 * This class provides methods for processing EZSP commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class EzspEnergyScanResultHandler extends EzspFrameResponse {
    public static final int FRAME_ID = 0x48;

    /**
     * The 802.15.4 channel number that was scanned
     * <p>
     * EZSP type is <i>uint8_t</i> - Java type is {@link int}
     */
    private int channel;

    /**
     * The maximum RSSI value found on the channel.
     * <p>
     * EZSP type is <i>int8s</i> - Java type is {@link int}
     */
    private int maxRssiValue;

    /**
     * Response and Handler constructor
     */
    public EzspEnergyScanResultHandler(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        // Deserialize the fields
        channel = deserializer.deserializeUInt8();
        maxRssiValue = deserializer.deserializeInt8S();
    }

    /**
     * The 802.15.4 channel number that was scanned
     * <p>
     * EZSP type is <i>uint8_t</i> - Java type is {@link int}
     *
     * @return the current channel as {@link int}
     */
    public int getChannel() {
        return channel;
    }

    /**
     * The 802.15.4 channel number that was scanned
     *
     * @param channel the channel to set as {@link int}
     */
    public void setChannel(int channel) {
        this.channel = channel;
    }

    /**
     * The maximum RSSI value found on the channel.
     * <p>
     * EZSP type is <i>int8s</i> - Java type is {@link int}
     *
     * @return the current maxRssiValue as {@link int}
     */
    public int getMaxRssiValue() {
        return maxRssiValue;
    }

    /**
     * The maximum RSSI value found on the channel.
     *
     * @param maxRssiValue the maxRssiValue to set as {@link int}
     */
    public void setMaxRssiValue(int maxRssiValue) {
        this.maxRssiValue = maxRssiValue;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(80);
        builder.append("EzspEnergyScanResultHandler [channel=");
        builder.append(channel);
        builder.append(", maxRssiValue=");
        builder.append(maxRssiValue);
        builder.append(']');
        return builder.toString();
    }
}