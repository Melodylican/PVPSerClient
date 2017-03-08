/**   
 * @文件名称: TestSingleTon.java
 * @类路径: com.dsky.netty.pvpser.test.client
 * @描述: TODO
 * @作者：chris.li(李灿)
 * @时间：2017年2月27日 下午2:41:24
 * @版本：V1.0   
 */
package com.dsky.netty.pvpser.client;

import java.text.SimpleDateFormat;

import com.dsky.netty.pvpser.common.ProtocolCode;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketRequest;
import com.dsky.netty.pvpser.protocode.PVPSerProtocol.SocketResponse;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：dsky
 * @作者：chris.li
 * @创建时间：2017年2月27日 下午2:41:24
 * @版本：V1.0
 */
public class TestSingleTon {
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int i=0;
		
		long start = System.currentTimeMillis();
		System.out.println("开始时间是： [ "+sf.format(start)+" ]");
		for(i=0;i<50;i++) {
        //准备发送的消息
		SocketRequest.Builder req = SocketRequest.newBuilder();
		req.setNumber(ProtocolCode.CREATE_ROOM);
		req.setUserId("123456789");
		req.setRoomId("9999999999");
		req.setRequestMsg("{\"gamedata\":\"dsfdsfdsfds\",\"userdata\":\"dsfdsfdsfds\",\"time\":300,\"roomCreatetime\":1321456421,\"numbers\":3}");

		PVPClientSingleton.getSingleton().sendRequest(req.build());
		
		SocketResponse rp = PVPClientSingleton.getSingleton().getResponse();
		System.out.println(i  +  "   "+System.currentTimeMillis());
		rp.getResponseMsg();
		System.out.println(i  +  "   "+System.currentTimeMillis());
		System.out.println("==========================================");
		
		}
		/*
		long end = System.currentTimeMillis();
		System.out.println("结束时间是： [ "+sf.format(end)+" ]");
		long totalTime = end-start;
		double min = (double)totalTime/100000;
		System.out.println("100000次请求及接收返回的总时间是 [ "+totalTime+" ]毫秒");
		System.out.println("100000次请求及接收返回的平均时间是：[ "+min+" ]毫秒");
		*/
	}

}
