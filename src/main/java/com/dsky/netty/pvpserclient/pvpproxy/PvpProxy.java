package com.dsky.netty.pvpserclient.pvpproxy;

/*
 *     PvpProxy.java
 *
 */

import org.apache.log4j.Logger;

/**
 *  PvpProxy Demo  
 */
public class PvpProxy {
	private  static String SUBTREE = "/client/";
	private  static String PUBTREE = "/client/A";
	private  static int INTERVAL = 10;
	private  static String IP="192.168.2.44";
	private  static String PORT="5556";
	
	
	private static final Logger logger = Logger.getLogger(PvpProxy.class);

	public void run() {
		/* 
		 *  1、收到订阅的消息，必须继承覆盖handler，注意ZmqHandlerMsg构造函数参数n，
		 *  如果消息队列满了（默认100000条），等待ns依然没有空间，则丢弃
		 */
		ZmqCliProxy zmqCliProx = new ZmqCliProxy(new ZmqHandlerMsg(1) {
			public int index;
			public int handler(String Subtree, byte[] context,long sequence) {
				long curtime = System.currentTimeMillis();
				
				String val = new String(context);
				
				//数据同步完成标志
				if(Subtree.equals("KTHXBAI"))
				{
					logger.info("KTHXBAI, seq:" +  sequence);
					return 1;
				}
				
				long time_consumption = 1;
				try {
					time_consumption = curtime - Long.parseLong(val);
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
					time_consumption = 0;
				}

				if (Subtree.indexOf(PUBTREE + "0") == 0) {
					index = 0;
				}

				String shallkey = PUBTREE + index;
				if (shallkey.equals(Subtree)) {
					logger.debug(" seq:" + sequence+ " Subtree:" + Subtree + " " + curtime + " - " + val 
							+ "=" + time_consumption);
				} else if (Subtree.indexOf(PUBTREE) == 0 && !shallkey.equals(Subtree)){
					logger.debug(" err seq:" + sequence + " Subtree:" + Subtree + " " + curtime + " - " + val
							+ "=" + time_consumption + " index:" + index);
				}

				if (shallkey.equals(Subtree)) {
					index++;
				}
				
				if( sequence %10000 == 0){
					logger.debug("seq:" + sequence + " Subtree:" + Subtree + " " + curtime + " - " + val
							+ "=" + time_consumption);
				}
				return 1;
			}
		});

		/* 
		 *  2、设置订阅的类型
		 */
		zmqCliProx.subtree(SUBTREE);
		/* 
		 *  3、连接发布服务器
		 */
		zmqCliProx.connect("tcp://"+IP, PORT);
	
		long i = 0;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		// Set random tuples into the distributed hash
		long start = System.currentTimeMillis();
		while (!Thread.currentThread().isInterrupted()) {
			// Set random value, check it was stored
			String pub = String.format("%s%d", PUBTREE, i);
			String value = String.format("%d", System.currentTimeMillis());
			
			/* 
			 *  4、发布订阅类型送消息
			 *  pub，发布的类型
			 *  value,发布的内容
			 *  0，缓存时间
			 */
			zmqCliProx.send(pub, value, 0);

			if (i % INTERVAL == 0) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
			
			if(i%10000==0)
				System.out.println(" start:" + start + " i:" + i);

			i++;
		}

		System.out.println(" start:" + start + " i:" + i);

		zmqCliProx.destroy();
	}

	public static void main(String[] args) {
		for (String s: args)
		{
			System.out.println(s);
		}
		
		System.out.println("args.length" + args.length);
		
		if(args.length == 5 ){
			PvpProxy.SUBTREE=args[0];
			PvpProxy.PUBTREE= args[1];
			PvpProxy.INTERVAL= Integer.parseInt(args[2]) ;
			PvpProxy.IP= args[3] ;
			PvpProxy.PORT= args[4] ;
		}
		
		System.out.println("Sub: " +   PvpProxy.SUBTREE );
		System.out.println("Key: " +   PvpProxy.PUBTREE );
		System.out.println("Interval: " +  PvpProxy.INTERVAL );
		System.out.println("IP: " +   PvpProxy.IP );
		System.out.println("PORT: " +  PvpProxy.PORT );
		
		new PvpProxy().run();
	}
}
