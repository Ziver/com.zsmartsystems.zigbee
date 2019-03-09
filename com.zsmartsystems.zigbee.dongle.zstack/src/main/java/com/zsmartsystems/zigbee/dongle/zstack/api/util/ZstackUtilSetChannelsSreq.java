/**
 * Copyright (c) 2016-2019 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.zigbee.dongle.zstack.api.util;

import com.zsmartsystems.zigbee.dongle.zstack.api.ZstackFrameRequest;
import com.zsmartsystems.zigbee.dongle.zstack.api.rpc.ZstackRpcSreqErrorSrsp;

/**
 * Class to implement the Z-Stack command <b>UTIL_SET_CHANNELS</b>.
 * <p>
 * This command is used to store a channel select bit-mask into Non-Volatile memory to be used the next time the target device
 * resets.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson
 */
public class ZstackUtilSetChannelsSreq extends ZstackFrameRequest {

    /**
     * A bit-mask representing the channel(s) to scan the next time the target device resets.
     */
    private int channels;

    /**
     * Request constructor
     */
    public ZstackUtilSetChannelsSreq() {
        synchronousCommand = true;
    }

    /**
     * A bit-mask representing the channel(s) to scan the next time the target device resets.
     *
     * @return the current channels as {@link int}
     */
    public int getChannels() {
        return channels;
    }

    /**
     * A bit-mask representing the channel(s) to scan the next time the target device resets.
     *
     * @param channels the Channels to set as {@link int}
     */
    public void setChannels(int channels) {
        this.channels = channels;
    }

    @Override
    public boolean matchSreqError(ZstackRpcSreqErrorSrsp response) {
        return (((response.getReqCmd0() & 0x1F) == ZSTACK_UTIL) && (response.getReqCmd1() == 0x03));
    }

    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(ZSTACK_SREQ, ZSTACK_UTIL, 0x03);

        // Serialize the fields
        serializeUInt32(channels);
        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(53);
        builder.append("ZstackUtilSetChannelsSreq [channels=");
        builder.append(channels);
        builder.append(']');
        return builder.toString();
    }
}