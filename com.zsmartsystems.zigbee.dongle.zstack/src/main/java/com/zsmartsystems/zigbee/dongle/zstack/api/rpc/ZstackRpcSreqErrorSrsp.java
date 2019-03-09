/**
 * Copyright (c) 2016-2019 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.zigbee.dongle.zstack.api.rpc;

import com.zsmartsystems.zigbee.dongle.zstack.api.ZstackFrameResponse;

/**
 * Class to implement the Z-Stack command <b>RPC_SREQ_ERROR</b>.
 * <p>
 * When the ZNP cannot recognize an SREQ command from the host processor, the following SRSP is returned.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson
 */
public class ZstackRpcSreqErrorSrsp extends ZstackFrameResponse {

    /**
     * The error code maps to one of the enumerated values.
     */
    private ZstackSreqErrorCode errorCode;

    /**
     * The Cmd0 value of the processed SREQ
     */
    private int reqCmd0;

    /**
     * The Cmd1 value of the processed SREQ
     */
    private int reqCmd1;

    /**
     * Response and Handler constructor
     */
    public ZstackRpcSreqErrorSrsp(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        synchronousCommand = true;

        // Deserialize the fields
        errorCode = ZstackSreqErrorCode.valueOf(deserializeUInt8());
        reqCmd0 = deserializeUInt8();
        reqCmd1 = deserializeUInt8();
    }

    /**
     * The error code maps to one of the enumerated values.
     *
     * @return the current errorCode as {@link ZstackSreqErrorCode}
     */
    public ZstackSreqErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * The error code maps to one of the enumerated values.
     *
     * @param errorCode the ErrorCode to set as {@link ZstackSreqErrorCode}
     */
    public void setErrorCode(ZstackSreqErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * The Cmd0 value of the processed SREQ
     *
     * @return the current reqCmd0 as {@link int}
     */
    public int getReqCmd0() {
        return reqCmd0;
    }

    /**
     * The Cmd0 value of the processed SREQ
     *
     * @param reqCmd0 the ReqCmd0 to set as {@link int}
     */
    public void setReqCmd0(int reqCmd0) {
        this.reqCmd0 = reqCmd0;
    }

    /**
     * The Cmd1 value of the processed SREQ
     *
     * @return the current reqCmd1 as {@link int}
     */
    public int getReqCmd1() {
        return reqCmd1;
    }

    /**
     * The Cmd1 value of the processed SREQ
     *
     * @param reqCmd1 the ReqCmd1 to set as {@link int}
     */
    public void setReqCmd1(int reqCmd1) {
        this.reqCmd1 = reqCmd1;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(100);
        builder.append("ZstackRpcSreqErrorSrsp [errorCode=");
        builder.append(errorCode);
        builder.append(", reqCmd0=");
        builder.append(String.format("%02X", reqCmd0));
        builder.append(", reqCmd1=");
        builder.append(String.format("%02X", reqCmd1));
        builder.append(']');
        return builder.toString();
    }
}