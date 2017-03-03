/**   
 * @文件名称: TestThread.java
 * @类路径: test
 * @描述: TODO
 * @作者：chris.li(李灿)
 * @时间：2017年3月2日 下午3:00:42
 * @版本：V1.0   
 */
package test;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：dsky
 * @作者：chris.li
 * @创建时间：2017年3月2日 下午3:00:42
 * @版本：V1.0
 */
public class TestThread {
	public static void main(String[] args) {
		 new Thread(new Runnable(){  
	            @Override  
	            public void run() {  
	            	while(true) {
	            		System.out.println("run()");
	            	}  
	            }},"r1").start();  
	}

}
