package day11;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 聊天室客户端
 * @author adminitartor
 *
 */
public class Client {
	/**
	 * java.net.Socket 套接字
	 * 封装了TCP通讯协议。使用它可以基于TCP与
	 * 远端计算机上的服务端应用程序连接并进行通讯。
	 * 
	 */
	private Socket socket;
	/**
	 * 构造方法，用来初始化客户端
	 * @throws Exception 
	 */
	public Client() throws Exception{
		try {
			/*
			 * 实例化Socket就是与服务端建立连接
			 * 的过程。这里需要传入两个参数来指定
			 * 服务端地址信息
			 * 参数1:服务端计算机的IP地址
			 * 参数2:运行在服务端计算机上的服务端
			 *       应用程序打开的服务端口。
			 * 通过IP可以找到服务端计算机，再通过
			 * 端口可以连接到运行在服务端计算机上
			 * 的服务端应用程序。
			 * 由于实例化就是连接过程，若服务端没有
			 * 响应，这里实例化Socket会抛出异常。      
			 */
			System.out.println("正在连接服务端...");
			socket = new Socket(
				"localhost",
				8088
			);
			System.out.println("与服务端建立连接!");
			
		} catch (Exception e) {
			/*
			 * 将来针对异常可能要记录日志，所以
			 * 需要感知错误。但是若异常不应当在
			 * 这里被处理时可以继续在catch中将其
			 * 抛出。
			 */
			throw e;
		}
	}
	/**
	 * 客户端开始工作的方法
	 */
	public void start(){
		try {
			Scanner scanner = new Scanner(System.in);
			/*
			 * Socket提供方法：
			 * OutputStream getOutputStream()
			 * 通过Socket获取的输出流写出的字节
			 * 都会通过网络发送给远端计算机。
			 * 这里就等于发送给服务端了。
			 */
			OutputStream out 
				= socket.getOutputStream();
			
			OutputStreamWriter osw
				= new OutputStreamWriter(
					out,"UTF-8");
			
			PrintWriter pw
				= new PrintWriter(osw,true);
			
			//将读取服务端消息的线程启动
			ServerHandler handler 
				= new ServerHandler();
			Thread t = new Thread(handler);
			t.start();		
			
			
			String line = null;
			
			long lastSend = System.currentTimeMillis();
			
			while(true){
				line = scanner.nextLine();
				if(System.currentTimeMillis()
						-lastSend>=1000
				){
					pw.println(line);
					lastSend = System.currentTimeMillis();
				}else{
					System.out.println("您说话速度太快，请休息一下...");
					lastSend = System.currentTimeMillis();
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("客户端运行失败!");
		}
	}
	/**
	 * 该线程负责读取服务端发送过来的所有消息并
	 * 输出到控制台。
	 * @author adminitartor
	 *
	 */
	private class ServerHandler implements Runnable{
		public void run(){
			try {
				//创建输入流
				InputStream in 
					= socket.getInputStream();
				InputStreamReader isr
					= new InputStreamReader(in,"UTF-8");
				BufferedReader br
					= new BufferedReader(isr);	
				
				String message = null;
				while((message = br.readLine())!=null){
					System.out.println(message);
				}
				
			} catch (Exception e) {
				
			}
		}
	}
	
}







