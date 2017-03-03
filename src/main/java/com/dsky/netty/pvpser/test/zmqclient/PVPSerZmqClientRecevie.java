/**   
 * @文件名称: PVPSerZmqClient.java
 * @类路径: com.dsky.netty.pvpser.test.zmqclient
 * @描述: TODO
 * @作者：chris.li(李灿)
 * @时间：2017年2月28日 下午5:22:55
 * @版本：V1.0   
 */
package com.dsky.netty.pvpser.test.zmqclient;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.dsky.netty.pvpser.client.PVPClientSingleton;
import com.dsky.netty.pvpser.common.ProtocolCode;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketRequest;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketResponse;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @类功能说明：用于处理从zmq中接收消息和向zmq中发送消息
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：dsky
 * @作者：chris.li
 * @创建时间：2017年2月28日 下午5:22:55
 * @版本：V1.0
 */
public class PVPSerZmqClientRecevie {
	private  static String SUBTREE = "/client/";
	private  static String KEYVAL = "/client/A";
	private  static int INTERVAL = 10;
	private  static String IP="192.168.2.44";
	private  static String PORT="5556";
	
	private static final Logger logger = Logger.getLogger(PvpProxy.class);

	public void run() throws IOException, InterruptedException {
		ZmqCliProxy zmqCliProx = new ZmqCliProxy(new ZmqHandlerMsg(10) {
			public int index;
			public int handler(kvmsg msg) {
				long curtime = System.currentTimeMillis();

				int size = msg.body().length;
				System.out.println(size+"===============================rec");
				byte[] buf = new byte[size];
				System.arraycopy(msg.body(), 0, buf, 0, size);
//				/*
				SocketResponse rp;
				try {
					if(buf.length >0) {
					try {
						PVPClientSingleton.getSingleton().sendRequest(SocketRequest.parseFrom(buf));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rp = PVPClientSingleton.getSingleton().getResponse();
					System.out.println(rp.getResponseMsg()+"        收到的消息 。。。。");
					} else {
						System.out.println("收到消息的字节数为 0");
					}
				} catch (InvalidProtocolBufferException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				String val = new String(buf);
				//System.out.println(val.length()+" ----------------------------------"+val);
				buf = null;

				String key = msg.getKey();
				
				//数据同步完成标志
				if(key.equals("KTHXBAI"))
				{
					logger.info("KTHXBAI, seq:" + msg.getSequence());
					return 1;
				}
				
				long time_consumption = 1;
				try {
					time_consumption = curtime - Long.parseLong(val);
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
					time_consumption = 0;
				}

				if (key.indexOf(KEYVAL + "0") == 0) {
					index = 0;
				}

				String shallkey = KEYVAL + index;
				if (shallkey.equals(key) ) {
					logger.debug(" seq:" + msg.getSequence() + " key:" + key + " " + curtime + " - " + val 
							+ "=" + time_consumption);
				} else if (key.indexOf(KEYVAL) == 0 && !shallkey.equals(key)){
					logger.debug(" err seq:" + msg.getSequence() + " key:" + key + " " + curtime + " - " + val
							+ "=" + time_consumption + " index:" + index);
				}

				if (shallkey.equals(key)) {
					index++;
				}
				
				if( msg.getSequence() %10000 == 0){
					logger.debug("seq:" + msg.getSequence() + " key:" + key + " " + curtime + " - " + val
							+ "=" + time_consumption);
				}
				return 1;
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		zmqCliProx.subtree(SUBTREE);
		zmqCliProx.connect("tcp://"+IP, PORT);
		// Set random tuples into the distributed hash
		while (!Thread.currentThread().isInterrupted()) {
			// Set random value, check it was stored
			// String key = String.format("%s%d", KEYVAL, i);
			// String value = String.format("%d", System.currentTimeMillis());
           // System.out.println("test ...");
			SocketRequest.Builder req = SocketRequest.newBuilder();
			req.setNumber(ProtocolCode.WAITTING_USER_JOIN_ROOM);
			req.setUserId("123456789");
			req.setRoomId("9999999999");
			req.setRequestMsg("{\"roomId\":154321321,\"userId\":123456,\"data\":\"sdfdsfsdffffffffffffffffffffffffffffffffffffffffffffffdsfdsvdsfdsfdsfdscvdsfdsfdsfds\",\"roomCreatetime\":1321456421,\"numbers\":3}");

			zmqCliProx.set("321432131", req.build().toByteArray(), 0);
			
			System.out.println(req.build().toByteArray().length+"++++++++++++++++++++++++++++++send");

		}
		zmqCliProx.destroy();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		new PVPSerZmqClientRecevie().run();

	}
}