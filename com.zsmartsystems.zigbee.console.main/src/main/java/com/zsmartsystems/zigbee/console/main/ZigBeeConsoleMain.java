/**
 * Copyright (c) 2016-2018 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.zigbee.console.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.LoggerFactory;

import com.zsmartsystems.zigbee.ExtendedPanId;
import com.zsmartsystems.zigbee.ZigBeeNetworkManager;
import com.zsmartsystems.zigbee.ZigBeeNetworkMeshMonitor;
import com.zsmartsystems.zigbee.ZigBeeNetworkStateSerializer;
import com.zsmartsystems.zigbee.console.ZigBeeConsoleCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleMmoHashCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleNcpChildrenCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleNcpConfigurationCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleNcpCountersCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleNcpStateCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleNcpValueCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleNcpVersionCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleSecurityStateCommand;
import com.zsmartsystems.zigbee.console.ember.EmberConsoleTransientKeyCommand;
import com.zsmartsystems.zigbee.console.telegesis.TelegesisConsoleSecurityStateCommand;
import com.zsmartsystems.zigbee.dongle.cc2531.ZigBeeDongleTiCc2531;
import com.zsmartsystems.zigbee.dongle.conbee.ZigBeeDongleConBee;
import com.zsmartsystems.zigbee.dongle.ember.ZigBeeDongleEzsp;
import com.zsmartsystems.zigbee.dongle.telegesis.ZigBeeDongleTelegesis;
import com.zsmartsystems.zigbee.dongle.xbee.ZigBeeDongleXBee;
import com.zsmartsystems.zigbee.security.ZigBeeKey;
import com.zsmartsystems.zigbee.serial.ZigBeeSerialPort;
import com.zsmartsystems.zigbee.serialization.DefaultDeserializer;
import com.zsmartsystems.zigbee.serialization.DefaultSerializer;
import com.zsmartsystems.zigbee.transport.ConcentratorConfig;
import com.zsmartsystems.zigbee.transport.ConcentratorType;
import com.zsmartsystems.zigbee.transport.TransportConfig;
import com.zsmartsystems.zigbee.transport.TransportConfigOption;
import com.zsmartsystems.zigbee.transport.ZigBeePort;
import com.zsmartsystems.zigbee.transport.ZigBeePort.FlowControl;
import com.zsmartsystems.zigbee.transport.ZigBeeTransportTransmit;
import com.zsmartsystems.zigbee.zcl.clusters.ZclIasZoneCluster;

/**
 * The ZigBee test console. Simple console used for testing the framework.
 *
 * @author Chris Jackson
 */
public class ZigBeeConsoleMain {
    /**
     * The {@link org.slf4j.Logger}.
     */
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ZigBeeConsoleMain.class);
    /**
     * The usage.
     */
    public static final String USAGE = "Syntax: java -jar com.zsmartsystems.zigbee.console.main.jar [EMBER|CC2531|TELEGESIS|CONBEE|XBEE] SERIALPORT SERIALBAUD CHANNEL PAN EPAN NETWORK_KEY RESET";

    /**
     * Private constructor to disable constructing main class.
     */
    private ZigBeeConsoleMain() {
    }

    /**
     * The main method.
     *
     * @param args the command arguments
     */
    public static void main(final String[] args) {
        DOMConfigurator.configure("./log4j.xml");

        final String serialPortName;
        final String dongleName;
        final Integer serialBaud;
        Integer channel;
        Integer pan;
        ExtendedPanId extendedPan;
        final TransportConfig transportOptions = new TransportConfig();
        boolean resetNetwork;
        FlowControl flowControl = null;

        Options options = new Options();
        options.addOption(Option.builder("d").longOpt("dongle").hasArg().argName("dongle type")
                .desc("Set the dongle type to use (EMBER | CC2531 | TELEGESIS | CONBEE | XBEE)").required().build());
        options.addOption(Option.builder("p").longOpt("port").argName("port name").hasArg().desc("Set the port")
                .required().build());
        options.addOption(
                Option.builder("b").longOpt("baud").hasArg().argName("baud").desc("Set the port baud rate").build());
        options.addOption(Option.builder("f").longOpt("flow").hasArg().argName("type")
                .desc("Set the flow control (none | hardware | software)").build());
        options.addOption(Option.builder("c").longOpt("channel").hasArg().argName("channel id")
                .desc("Set the ZigBee channel ID").build());
        options.addOption(
                Option.builder("a").longOpt("pan").hasArg().argName("PAN ID").desc("Set the ZigBee PAN ID").build());
        options.addOption(
                Option.builder("e").longOpt("epan").hasArg().argName("EPAN ID").desc("Set the ZigBee EPAN ID").build());
        options.addOption(Option.builder("n").longOpt("nwkkey").hasArg().argName("key")
                .desc("Set the ZigBee Network key (defaults to randon value)").build());
        options.addOption(Option.builder("l").longOpt("linkkey").hasArg().argName("key")
                .desc("Set the ZigBee Link key (defaults to well known ZHA key)").build());
        options.addOption(Option.builder("r").longOpt("reset").desc("Reset the ZigBee dongle").build());
        options.addOption(Option.builder("?").longOpt("help").desc("Print usage information").build());

        CommandLine cmdline;
        try {
            CommandLineParser parser = new DefaultParser();
            cmdline = parser.parse(options, args);

            if (cmdline.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("zigbeeconsole", options);
                return;
            }

            if (!cmdline.hasOption("dongle")) {
                System.err.println("Dongle must be specified with the 'dongle' option");
                return;
            }
            if (!cmdline.hasOption("port")) {
                System.err.println("Serial port must be specified with the 'port' option");
                return;
            }

            dongleName = cmdline.getOptionValue("dongle");
            serialPortName = cmdline.getOptionValue("port");
            serialBaud = parseDecimalOrHexInt(cmdline.getOptionValue("baud"));
            resetNetwork = cmdline.hasOption("reset");

            if (cmdline.hasOption("flow")) {
                switch (cmdline.getOptionValue("flow").toLowerCase()) {
                    case "software":
                        flowControl = FlowControl.FLOWCONTROL_OUT_XONOFF;
                        break;
                    case "hardware":
                        flowControl = FlowControl.FLOWCONTROL_OUT_RTSCTS;
                        break;
                    case "none":
                        flowControl = FlowControl.FLOWCONTROL_OUT_NONE;
                        break;
                    default:
                        System.err.println(
                                "Unknown flow control option used: " + cmdline.getOptionValue("flow").toLowerCase());
                        return;
                }
            }
        } catch (ParseException exp) {
            System.err.println("Parsing command line failed.  Reason: " + exp.getMessage());
            return;
        }

        // Default the flow control based on the dongle
        if (flowControl == null) {
            switch (dongleName.toUpperCase()) {
                case "EMBER":
                    flowControl = FlowControl.FLOWCONTROL_OUT_RTSCTS;
                    break;
                default:
                    flowControl = FlowControl.FLOWCONTROL_OUT_NONE;
                    break;
            }
        }

        final ZigBeePort serialPort = new ZigBeeSerialPort(serialPortName, serialBaud, flowControl);

        System.out.println("Initialising ZigBee console...");

        List<Class<? extends ZigBeeConsoleCommand>> commands = new ArrayList<>();

        final ZigBeeTransportTransmit dongle;
        if (dongleName.toUpperCase().equals("CC2531")) {
            dongle = new ZigBeeDongleTiCc2531(serialPort);
        } else if (dongleName.toUpperCase().equals("EMBER")) {
            dongle = new ZigBeeDongleEzsp(serialPort);

            // Configure the concentrator
            // Max Hops defaults to system max
            ConcentratorConfig concentratorConfig = new ConcentratorConfig();
            concentratorConfig.setType(ConcentratorType.LOW_RAM);
            concentratorConfig.setMaxFailures(8);
            concentratorConfig.setMaxHops(0);
            concentratorConfig.setRefreshMinimum(60);
            concentratorConfig.setRefreshMaximum(3600);
            transportOptions.addOption(TransportConfigOption.CONCENTRATOR_CONFIG, concentratorConfig);

            // Add transport specific console commands
            commands.add(EmberConsoleNcpChildrenCommand.class);
            commands.add(EmberConsoleNcpConfigurationCommand.class);
            commands.add(EmberConsoleNcpCountersCommand.class);
            commands.add(EmberConsoleTransientKeyCommand.class);
            commands.add(EmberConsoleMmoHashCommand.class);
            commands.add(EmberConsoleNcpStateCommand.class);
            commands.add(EmberConsoleNcpStateCommand.class);
            commands.add(EmberConsoleNcpValueCommand.class);
            commands.add(EmberConsoleNcpVersionCommand.class);
            commands.add(EmberConsoleSecurityStateCommand.class);
        } else if (dongleName.toUpperCase().equals("XBEE")) {
            dongle = new ZigBeeDongleXBee(serialPort);
        } else if (dongleName.toUpperCase().equals("CONBEE")) {
            dongle = new ZigBeeDongleConBee(serialPort);
        } else if (dongleName.toUpperCase().equals("TELEGESIS")) {
            ZigBeeDongleTelegesis telegesisDongle = new ZigBeeDongleTelegesis(serialPort);
            telegesisDongle.setTelegesisPassword("password");
            dongle = telegesisDongle;

            Set<Integer> clusters = new HashSet<Integer>();
            clusters.add(ZclIasZoneCluster.CLUSTER_ID);
            transportOptions.addOption(TransportConfigOption.SUPPORTED_OUTPUT_CLUSTERS, clusters);

            commands.add(TelegesisConsoleSecurityStateCommand.class);
        } else {
            dongle = null;
        }

        if (dongle == null) {
            System.out.println("Dongle unable to be opened.");
            System.out.println(USAGE);
            return;
        }

        ZigBeeNetworkManager networkManager = new ZigBeeNetworkManager(dongle);

        ZigBeeNetworkStateSerializer networkStateSerializer = new ZigBeeNetworkStateSerializerImpl(dongleName);
        if (resetNetwork) {
            // networkStateSerializer.remove();
        }
        networkManager.setNetworkStateSerializer(networkStateSerializer);
        networkManager.setSerializer(DefaultSerializer.class, DefaultDeserializer.class);
        final ZigBeeConsole console = new ZigBeeConsole(networkManager, dongle, commands);

        // Initialise the network
        networkManager.initialize();

        System.out.println("PAN ID          = " + networkManager.getZigBeePanId());
        System.out.println("Extended PAN ID = " + networkManager.getZigBeeExtendedPanId());
        System.out.println("Channel         = " + networkManager.getZigBeeChannel());

        if (resetNetwork == true) {
            ZigBeeKey nwkKey;
            ZigBeeKey linkKey;

            if (cmdline.hasOption("channel")) {
                channel = parseDecimalOrHexInt(cmdline.getOptionValue("channel"));
            } else {
                channel = 11;
            }
            if (cmdline.hasOption("pan")) {
                pan = parseDecimalOrHexInt(cmdline.getOptionValue("pan"));
            } else {
                pan = 1;
            }
            if (cmdline.hasOption("epan")) {
                extendedPan = new ExtendedPanId(cmdline.getOptionValue("epan"));
            } else {
                extendedPan = new ExtendedPanId();
            }

            if (cmdline.hasOption("nwkkey")) {
                nwkKey = new ZigBeeKey(cmdline.getOptionValue("nwkkey"));
            } else {
                nwkKey = ZigBeeKey.createRandom();
            }
            if (cmdline.hasOption("linkkey")) {
                linkKey = new ZigBeeKey(cmdline.getOptionValue("linkkey"));
            } else {
                linkKey = new ZigBeeKey(new int[] { 0x5A, 0x69, 0x67, 0x42, 0x65, 0x65, 0x41, 0x6C, 0x6C, 0x69, 0x61,
                        0x6E, 0x63, 0x65, 0x30, 0x39 });
            }

            System.out.println("*** Resetting network");
            System.out.println("  * Channel          = " + channel);
            System.out.println("  * PAN ID           = " + pan);
            System.out.println("  * Extended PAN ID  = " + extendedPan);
            System.out.println("  * Link Key         = " + linkKey);
            System.out.println("  * Network Key      = " + nwkKey);

            networkManager.setZigBeeChannel(channel);
            networkManager.setZigBeePanId(pan);
            networkManager.setZigBeeExtendedPanId(extendedPan);
            networkManager.setZigBeeNetworkKey(nwkKey);
            networkManager.setZigBeeLinkKey(linkKey);
        }

        dongle.updateTransportConfig(transportOptions);

        if (!networkManager.startup(resetNetwork)) {
            System.out.println("ZigBee console starting up ... [FAIL]");
            // return;
        } else {
            System.out.println("ZigBee console starting up ... [OK]");
        }

        // Start the mesh monitor
        ZigBeeNetworkMeshMonitor meshMonitor = new ZigBeeNetworkMeshMonitor(networkManager);
        meshMonitor.startup(60);

        networkManager.addSupportedCluster(ZclIasZoneCluster.CLUSTER_ID);

        console.start();

        System.out.println("Console closed.");
    }

    /**
     * Parse decimal or hexadecimal integer.
     *
     * @param strVal the string value to parse
     * @return the parsed integer value
     */
    private static int parseDecimalOrHexInt(String strVal) {
        int radix = 10;
        String number = strVal;
        if (number.startsWith("0x")) {
            number = number.substring(2);
            radix = 16;
        }
        return Integer.parseInt(number, radix);
    }
}
