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
 * Class to implement the Z-Stack command <b>UTIL_SET_SECLEVEL</b>.
 * <p>
 * This command is used to store a channel select bit-mask into Non-Volatile memory to be used the next time the target device
 * resets.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson
 */
public class ZstackUtilSetSeclevelSreq extends ZstackFrameRequest {

    /**
     * Security level to use the next time the target device resets. Zero is used to disable security.
     */
    private int secLevel;

    /**
     * Request constructor
     */
    public ZstackUtilSetSeclevelSreq() {
        synchronousCommand = true;
    }

    /**
     * Security level to use the next time the target device resets. Zero is used to disable security.
     *
     * @return the current secLevel as {@link int}
     */
    public int getSecLevel() {
        return secLevel;
    }

    /**
     * Security level to use the next time the target device resets. Zero is used to disable security.
     *
     * @param secLevel the SecLevel to set as {@link int}
     */
    public void setSecLevel(int secLevel) {
        this.secLevel = secLevel;
    }

    @Override
    public boolean matchSreqError(ZstackRpcSreqErrorSrsp response) {
        return (((response.getReqCmd0() & 0x1F) == ZSTACK_UTIL) && (response.getReqCmd1() == 0x04));
    }

    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(ZSTACK_SREQ, ZSTACK_UTIL, 0x04);

        // Serialize the fields
        serializeUInt8(secLevel);
        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(53);
        builder.append("ZstackUtilSetSeclevelSreq [secLevel=");
        builder.append(secLevel);
        builder.append(']');
        return builder.toString();
    }
}