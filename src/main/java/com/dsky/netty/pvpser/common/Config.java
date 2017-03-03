package com.dsky.netty.pvpser.common;

import com.dsky.netty.pvpser.utils.ProReaderUtil;


public class Config {

    public static String ZMQ_IP = ProReaderUtil.getInstance().getZMQPro().get("zmq.ip");

    public static String ZMQ_PORT = ProReaderUtil.getInstance().getZMQPro().get("zmq.port");

    public static int ZMQ_INTERVAL = Integer.parseInt(ProReaderUtil.getInstance().getZMQPro().get("zmq.interval"));

    public static String ZMQ_SUBTREE = ProReaderUtil.getInstance().getZMQPro().get("zmq.subtree");
 
    public static String ZMQ_KEYVAL = ProReaderUtil.getInstance().getZMQPro().get("zmq.keyval");
    
    public static String PVPSER_IP = ProReaderUtil.getInstance().getZMQPro().get("pvpser.ip");
    
    public static int PVPSER_PORT = Integer.parseInt(ProReaderUtil.getInstance().getZMQPro().get("pvpser.port"));

}
