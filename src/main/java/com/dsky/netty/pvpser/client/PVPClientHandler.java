/**   
 * @文件名称: GateClientHandler.java
 * @类路径: com.dsky.netty.serverTest
 * @描述: TODO
 * @作者：chris.li(李灿)
 * @时间：2017年2月20日 下午3:30:34
 * @版本：V1.0   
 */
package com.dsky.netty.pvpser.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.dsky.netty.pvpser.common.ProtocolCode;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketRequest;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketResponse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：dsky
 * @作者：chris.li
 * @创建时间：2017年2月20日 下午3:30:34
 * @版本：V1.0
 */
public class PVPClientHandler extends
		SimpleChannelInboundHandler<SocketResponse> {

	private Channel channel;
	private SocketResponse resp;
	BlockingQueue<SocketResponse> resps = new LinkedBlockingQueue<SocketResponse>();

	public SocketResponse getSocketResponse() {
		// Now wait for response from server
		boolean interrupted = false;
		//直到有消息才会返回，否则等待
		for (;;) {
			try {
				resp = resps.take();
				break;
			} catch (InterruptedException ignore) {
				interrupted = true;
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}
		return resp;
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		//System.out.println("[client] -- 测试点调用了这个方法 。。。");
		channel = ctx.channel();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, SocketResponse msg)
			throws Exception {
	    //将来自PVPSer服务返回的消息存放到队列当中
		//System.out.println("调用了这个方法");
		resps.add(msg);
	}

}
