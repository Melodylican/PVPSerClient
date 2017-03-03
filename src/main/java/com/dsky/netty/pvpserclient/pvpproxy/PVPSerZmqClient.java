/**   
 * @文件名称: PVPSerZmqClient.java
 * @类路径: com.dsky.netty.pvpserclient.pvpproxy
 * @描述: TODO
 * @作者：chris.li(李灿)
 * @时间：2017年3月2日 下午1:45:52
 * @版本：V1.0   
 */
package com.dsky.netty.pvpserclient.pvpproxy;

import com.dsky.netty.pvpser.client.PVPClientSingleton;
import com.dsky.netty.pvpser.common.Config;
import com.dsky.netty.pvpser.common.ProtocolCode;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketRequest;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketResponse;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：dsky
 * @作者：chris.li
 * @创建时间：2017年3月2日 下午1:45:52
 * @版本：V1.0
 */
public class PVPSerZmqClient {
	private static String SUBTREE = Config.ZMQ_SUBTREE;
	private static String PUBTREE = Config.ZMQ_KEYVAL;
	private static int INTERVAL = Config.ZMQ_INTERVAL;
	private static String IP = Config.ZMQ_IP;
	private static String PORT = Config.ZMQ_PORT;
	private static ZmqCliProxy zmqCliProx = null;

	public void start() {
		zmqCliProx = new ZmqCliProxy(new ZmqHandlerMsg(INTERVAL) {

			public int handler(String Subtree, byte[] context, long sequence) {
				//SocketResponse rp;
				try {
					if (context.length > 0) {
						// 将接收到的消息发送到PVPSer中 TODO 需要做contex合法性的判断
						SocketRequest req = SocketRequest.parseFrom(context);
						System.out.println(req.getRequestMsg()+"             ++++++++++++++++++++++");
						try {
							PVPClientSingleton.getSingleton().sendRequest(req);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {

						System.out.println("收到消息的字节数为 0");
					}
				} catch (InvalidProtocolBufferException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		new PVPSerZmqClient().start();
		
		// 单独启动一个用于接收来自服务器端返回消息的线程
		Thread receiveMsgFromPVPSer = new Thread() {
			@Override
			public void run() {
				while (true) {

					//直到取到消息才会继续执行 否则线程阻塞
					SocketResponse rp = PVPClientSingleton.getSingleton().getResponse();
					//System.out.println("从PVPSer中获取消息"+rp.getResponseMsg());
					if (rp != null) {
						System.out.println(Config.ZMQ_KEYVAL+rp.getRoomId()+"       =====");
						zmqCliProx.send(rp.getRoomId(), rp.toByteArray(), 0);

					} 
					/*
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
					*/

					System.out.println("调用这个方法");
					SocketRequest.Builder req = SocketRequest.newBuilder();
					req.setNumber(ProtocolCode.CREATE_ROOM);
					req.setUserId("1");
					req.setRoomId("99");
					req.setRequestMsg("{\"roomId\":154321321,\"userId\":123456,\"gamedata\":\"dsfdsfdsfds\",\"userdata\":\"dsfdsfdsfds\",\"time\":300,\"roomCreatetime\":1321456421,\"numbers\":3}");
					zmqCliProx.send(Config.ZMQ_KEYVAL+req.getRoomId(), req.build().toByteArray(), 0);
					
				}
			}
		};
		receiveMsgFromPVPSer.start();
	}
}
