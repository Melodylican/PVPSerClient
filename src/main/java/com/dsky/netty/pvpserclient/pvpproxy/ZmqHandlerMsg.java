package com.dsky.netty.pvpserclient.pvpproxy;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ZmqHandlerMsg extends Thread {

	private static final Logger logger = Logger.getLogger(ZmqHandlerMsg.class);
	private SimpleCache<kvmsg> MsgCache;
	private int timeout;

	public ZmqHandlerMsg() {
		timeout = 1000;
		MsgCache = new SimpleCache<kvmsg>();
		start();
	}

	public ZmqHandlerMsg(int timeout) {
		this.timeout = timeout;
		MsgCache = new SimpleCache<kvmsg>();
		start();
	}

	public int handler(String Subtree, byte[] context,long sequence) {
		logger.info("subtree:" + Subtree + "context length:" + context.length + 
				"sequence:" + sequence);
		return 0;
	}

	public boolean put(kvmsg msg) {
		if (null == msg)
			return false;
		return MsgCache.put(msg, timeout, TimeUnit.MILLISECONDS);
	}

	public void run() {
		while (true) {
			kvmsg msg = MsgCache.poll();
			if (null != msg) {
				if (msg.body() != null) {
					int size = msg.body().length;
					byte[] body = new byte[size];
					String key = msg.getKey();
					System.arraycopy(msg.body(), 0, body, 0, size);
					handler(key, body,msg.getSequence());
					body = null;
				}
				else{
					logger.info("key:" + msg.getKey() + "body is null");
				}
			}
			msg = null;
		}
	}
}
