/**
 * Copyright (c) 2016-2019 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.zigbee.zdo.command;

import javax.annotation.Generated;

import com.zsmartsystems.zigbee.zdo.ZdoResponse;

/**
 * Extended Simple Descriptor Response value object class.
 * <p>
 * <p>
 * The Extended_Simple_Desc_rsp is generated by a remote device in response to an
 * Extended_Simple_Desc_req directed to the remote device. This command shall be unicast to
 * the originator of the Extended_Simple_Desc_req command.
 * <p>
 * Code is auto-generated. Modifications may be overwritten!
 */
@Generated(value = "com.zsmartsystems.zigbee.autocode.ZigBeeCodeGenerator", date = "2019-04-13T11:59:37Z")
public class ExtendedSimpleDescriptorResponse extends ZdoResponse {
    /**
     * The cluster ID to which this command belongs.
     */
    public static int CLUSTER_ID = 0x0000;

    /**
     * Default constructor.
     */
    public ExtendedSimpleDescriptorResponse() {
        clusterId = CLUSTER_ID;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(35);
        builder.append("ExtendedSimpleDescriptorResponse [");
        builder.append(super.toString());
        builder.append(']');
        return builder.toString();
    }

}
