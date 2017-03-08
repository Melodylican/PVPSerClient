/**   
 * @文件名称: PVPClientSingleton.java
 * @类路径: com.dsky.netty.pvpser.test.client
 * @描述: TODO
 * @作者：chris.li(李灿)
 * @时间：2017年2月27日 下午2:30:06
 * @版本：V1.0   
 */
package com.dsky.netty.pvpser.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.dsky.netty.pvpser.common.Config;
import com.dsky.netty.pvpser.common.ProtocolCode;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketRequest;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketRequest.Builder;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketResponse;

/**
 * @类功能说明： 单例类 （双重校验锁）
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：dsky
 * @作者：chris.li
 * @创建时间：2017年2月27日 下午2:30:06
 * @版本：V1.0
 */
public class PVPClientSingleton {
	private volatile static PVPClientSingleton singleton;
	static final String HOST = Config.PVPSER_IP;
	static final int PORT = Config.PVPSER_PORT;
	
	private static EventLoopGroup group = null;
	private static Bootstrap bootstrap = null;
	private static Channel c = null;
	private static PVPClientHandler handler = null;
	
	private PVPClientSingleton(){}
	public static PVPClientSingleton getSingleton() {
		if(singleton == null) {
			synchronized(PVPClientSingleton.class) {
				if(singleton == null) {
					singleton = new PVPClientSingleton();
					//TODO 将连接创建在这里
					if( null == group) {
						group = new NioEventLoopGroup();
					}
					try{
						if(null == bootstrap) {
							bootstrap = new Bootstrap();
							bootstrap.group(group).channel(NioSocketChannel.class).handler(new PVPClientInitializer());
						}
						if(null == c) {
							//创建连接
							c = bootstrap.connect(HOST,PORT).sync().channel();
						}
						if(null == handler) {
							handler = c.pipeline().get(PVPClientHandler.class);
						}
					}catch(InterruptedException e) {
						//TODO
						System.out.println(e.getMessage());
					}
				}
			}
		}
		return singleton;
	}
	
	public static void sendRequest(SocketRequest socketRequest) throws InterruptedException {
		// 发送请求
		c.writeAndFlush(socketRequest);
	}
	
	public static SocketResponse getResponse() {
		//System.out.println("调用获取回复信息方法");
		SocketResponse resp = handler.getSocketResponse();
		return resp;
	}
	
	public static void shutDown() {
		group.shutdownGracefully();
	}

/*	
	public static void send(SocketRequest socketRequest) throws InterruptedException {
		
		try{
			
			bootstrap.group(group).channel(NioSocketChannel.class).handler(new PVPClientInitializer());
			//创建连接
			c = bootstrap.connect(HOST,PORT).sync().channel();
			
			//获取一个handle 来发送消息
			PVPClientHandler handler = c.pipeline().get(PVPClientHandler.class);
			
			System.out.println("调用了sendRequest 方法 ...");

			// 发送请求
			c.writeAndFlush(socketRequest);
			System.out.println("[client] -- 发送的请求信息体是： "+socketRequest.getRequestMsg());

			while(true) {
				SocketResponse resp = handle.sendRequest();
				
				if(resp.getResponseMsg() == null )
					return null;
				else {
					System.out.println("Got reponse msg from Server : "+resp.getResponseMsg());
					//c.close();
					return resp;
				}
			}
		} finally {
			group.shutdownGracefully();
		}
	}
*/
}
