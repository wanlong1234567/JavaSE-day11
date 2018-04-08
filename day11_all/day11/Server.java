package day11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * �����ҷ����
 * @author adminitartor
 *
 */
public class Server {
	/*
	 * �����ڷ���˵�java.net.ServerSocket
	 * ��Ҫ����������:
	 * 1:��ϵͳ�������ķ���˿ڣ��ͻ���Socket��
	 *   ��ͨ������˿������˳��������ӵġ�
	 * 2:�����÷���˿ڣ�һ��һ���ͻ���Socketͨ��
	 *   �ö˿ڳ��Խ������ӣ�ServerSocket�ͻ��֪
	 *   ��ʵ����һ��Socket��ÿͻ��˽���ͨѶ��  
	 */
	private ServerSocket server;
	/*
	 * ������пͻ�������������ڹ㲥��Ϣ��
	 * ���пͻ���
	 */
	private List<PrintWriter> allOut;
	
	/**
	 * ���췽����������ʼ�������
	 * @throws Exception 
	 */
	public Server() throws Exception{
		try {
			/*
			 * ʵ����ServerSocket��ͬʱ��ϵͳ
			 * �������˿ڣ�����ǰϵͳ����Ӧ��
			 * ��������ʹ������˿ڣ���ô����
			 * ʵ�������׳���ַ��ռ�õ��쳣��
			 * Address Already In Use
			 */
			System.out.println("�������������...");
			server = new ServerSocket(8088);
			allOut = new ArrayList<PrintWriter>();
			System.out.println("������������.");
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * ����˿�ʼ�����ķ���
	 */
	public void start(){
		try {
			/*
			 * ServerSocket�ṩ����:
			 * Socket accept()
			 * �÷�����һ���������������ڼ���
			 * ����˿ڣ�ֱ��һ���ͻ���������
			 * Ϊֹ������᷵��һ��Socket��ͨ
			 * �����Socket�Ϳ�����ÿͻ��˽�
			 * ��ͨѶ�ˡ�
			 * 
			 */
			while(true){
				System.out.println("�ȴ��ͻ�������...");
				Socket socket = server.accept();
				System.out.println("һ���ͻ���������!");
				
				//һ���ͻ������Ӻ�����һ���̴߳���ÿͻ�����Ϣ
				ClientHandler handler 
					= new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���������ʧ��!");
		}
	}
	/**
	 * ���̸߳���ͨ��������Socket��ָ���ͻ���
	 * ����ͨѶ
	 * @author adminitartor
	 *
	 */
	private class ClientHandler 
						implements Runnable{
		private Socket socket;
		
		//�ͻ��˵ĵ�ַ��Ϣ
		private String host;
		
		public ClientHandler(Socket socket){
			this.socket = socket;
			//ͨ����Socket��ȡԶ�˼������ַ��Ϣ
			InetAddress address
				= socket.getInetAddress();
			//��ȡ�ͻ���IP��ַ���ַ�����ʽ����
			host = address.getHostAddress();
		}
		
		public void run(){
			PrintWriter pw = null;
			try {
				/*
				 * ͨ��Socket��ȡ����������ȡ�����ݾ�������
				 * Զ�˼�������͹��������ݡ������൱�ڶ�ȡ
				 * ���ǿͻ��˷��͹��������ݡ�
				 */
				InputStream in 
					= socket.getInputStream();	
				InputStreamReader isr
					= new InputStreamReader(in,"UTF-8");			
				BufferedReader br
					= new BufferedReader(isr);
				
				//ͨ��Socket��ȡ��������ڽ���Ϣ���͸��ͻ���
				OutputStream out
					= socket.getOutputStream();
				OutputStreamWriter osw
					= new OutputStreamWriter(out,"UTF-8");
				pw = new PrintWriter(osw,true);
				
				synchronized (allOut) {
					//���ÿͻ��˵���������빲����
					allOut.add(pw);
				}
				
				
				String message = null;
				while((message = br.readLine())!=null){
					/*
					 * ��ʹ�û�������ȡһ�����Կͻ���
					 * ���͹������ַ��������У�br.readLine
					 * ������һֱ������ֱ���ͻ��˷�����һ��
					 * �ַ��������ͻ��˶Ͽ����ӣ���ô�ͻ���
					 * ��ϵͳ��ͬ����ķ�ӦҲ��ͬ��
					 * ��windows�Ŀͻ��˶Ͽ�ʱ,br.readLine
					 * �������׳��쳣��
					 * ��linux�Ŀͻ��˶Ͽ�ʱ,br.readLine
					 * �����᷵��null��
					 * 
					 */
//					message = br.readLine();
//					System.out.println("�ͻ���˵:"+message);
//					pw.println("�ͻ���˵:"+message);
					
					synchronized (allOut) {
						//���������ϣ�����Ϣ�������пͻ���
						for(PrintWriter o : allOut){
							o.println(host+"˵:"+message);
						}
					}
				}
			} catch (Exception e) {
				
			} finally{
				//����ͻ��˶Ͽ����Ӻ�Ĳ���
				synchronized (allOut) {		
					//1:�Ƚ��ÿͻ��˵�������ӹ�����ɾ��
					allOut.remove(pw);
				}
				
				//����Ӧ�ÿͻ��˵�Socket�ر����ͷ���Դ
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}









