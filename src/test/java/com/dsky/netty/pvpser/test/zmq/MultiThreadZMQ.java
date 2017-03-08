/**   
 * @文件名称: MultiThreadZMQ.java
 * @类路径: com.dsky.netty.pvpser.test.zmq
 * @描述: TODO
 * @作者：chris.li(李灿)
 * @时间：2017年3月8日 下午5:46:05
 * @版本：V1.0   
 */
package com.dsky.netty.pvpser.test.zmq;

import org.apache.log4j.Logger;

import com.dsky.netty.pvpser.client.PVPClientSingleton;
import com.dsky.netty.pvpser.common.Config;
import com.dsky.netty.pvpser.common.ProtocolCode;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketRequest;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketResponse;
import com.dsky.netty.pvpserclient.pvpproxy.PVPSerZmqClient;
import com.dsky.netty.pvpserclient.pvpproxy.ZmqCliProxy;
import com.dsky.netty.pvpserclient.pvpproxy.ZmqHandlerMsg;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：dsky
 * @作者：chris.li
 * @创建时间：2017年3月8日 下午5:46:05
 * @版本：V1.0
 */
public class MultiThreadZMQ {
	private static String SUBTREE = Config.ZMQ_SUBTREE;
	private static String PUBTREE = Config.ZMQ_KEYVAL;
	private static int INTERVAL = Config.ZMQ_INTERVAL;
	private static String IP = Config.ZMQ_IP;
	private static String PORT = Config.ZMQ_PORT;
	private static ZmqCliProxy zmqCliProx = null;
	
	private static Logger logger = Logger.getLogger(PVPSerZmqClient.class);

	public void start() {
		zmqCliProx = new ZmqCliProxy(new ZmqHandlerMsg(INTERVAL) {

			public int handler(String Subtree, byte[] context, long sequence) {
				//System.out.println("  ----------------  ");
				/*
				if (context.length > 0) {
					// 将接收到的消息发送到PVPSer中 TODO 需要做contex合法性的判断
					SocketRequest req = null;
					try{
						req = SocketRequest.parseFrom(context);
						try {
							if(req.getRequestMsg() != null) {
							PVPClientSingleton.getSingleton().sendRequest(req);
							System.out.println("请求命令： ["+req.getNumber()+"]   ------   收到消息时间： ["+System.currentTimeMillis()+"]");
						    }
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch(InvalidProtocolBufferException e) {
						System.out.println("转换后出现异常！");
						 logger.debug(e.getMessage());
					}
				} else {

					System.out.println("收到消息的字节数为 0");
				}
				*/
				return 0;
			}

		});

		/*
		 * 2、设置订阅的类型
		 */
		zmqCliProx.subtree(SUBTREE);
		/*
		 * 3、连接发布服务器
		 */
		zmqCliProx.connect("tcp://" + IP, PORT);
	}

	public static void main(String[] args) {
		new MultiThreadZMQ().start();
		
		// 单独启动一个用于接收来自服务器端返回消息的线程
		Thread receiveMsgFromPVPSer1 = new Thread() {
			@Override
			public void run() {
				while (true) {
					//直到取到消息才会继续执行 否则线程阻塞
///*
					System.out.println("调用这个方法");
					SocketRequest.Builder req = SocketRequest.newBuilder();
					req.setNumber(ProtocolCode.CREATE_ROOM);
					req.setUserId("1");
					req.setRoomId("99");
					req.setRequestMsg("{\"roomId\":154321321,\"userId\":123456,\"gamedata\":\"dsfdsfdsfds\",\"userdata\":\"dsfdsfdsfds\",\"time\":300,\"roomCreatetime\":1321456421,\"numbers\":3}");
					zmqCliProx.send(Config.ZMQ_KEYVAL+req.getRoomId(), req.build().toByteArray(), 0);
//*/					
				}
			}
		};
		Thread receiveMsgFromPVPSer2 = new Thread() {
			@Override
			public void run() {
				while (true) {
					//直到取到消息才会继续执行 否则线程阻塞
///*
					System.out.println("调用这个方法");
					SocketRequest.Builder req = SocketRequest.newBuilder();
					req.setNumber(ProtocolCode.CREATE_ROOM);
					req.setUserId("1");
					req.setRoomId("99");
					req.setRequestMsg("{\"roomId\":154321321,\"userId\":123456,\"gamedata\":\"dsfdsfdsfds\",\"userdata\":\"dsfdsfdsfds\",\"time\":300,\"roomCreatetime\":1321456421,\"numbers\":3}");
					zmqCliProx.send(Config.ZMQ_KEYVAL+req.getRoomId(), req.build().toByteArray(), 0);
//*/					
				}
			}
		};
		Thread receiveMsgFromPVPSer3 = new Thread() {
			@Override
			public void run() {
				int i=0;
				while (true) {
					//直到取到消息才会继续执行 否则线程阻塞
///*
					SocketRequest.Builder req = SocketRequest.newBuilder();
					req.setNumber(ProtocolCode.CREATE_ROOM);
					req.setUserId("1");
					req.setRoomId("99");
					req.setRequestMsg("{\"roomId\":154321321,\"userId\":123456,\"gamedata\":\"dsfdsfdsfds\",\"userdata\":\"dsfdsfdsfds\",\"time\":300,\"roomCreatetime\":1321456421,\"numbers\":3}");
					zmqCliProx.send(Config.ZMQ_KEYVAL+req.getRoomId(), req.build().toByteArray(), 0);
					if(i%10000 ==0)
						System.out.println(i +"         "+System.currentTimeMillis());
					i++;
//*/					
				}
			}
		};
		receiveMsgFromPVPSer3.start();
		//receiveMsgFromPVPSer2.start();
		//receiveMsgFromPVPSer3.start();
	}
}

