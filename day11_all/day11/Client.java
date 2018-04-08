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
 * �����ҿͻ���
 * @author adminitartor
 *
 */
public class Client {
	/**
	 * java.net.Socket �׽���
	 * ��װ��TCPͨѶЭ�顣ʹ�������Ի���TCP��
	 * Զ�˼�����ϵķ����Ӧ�ó������Ӳ�����ͨѶ��
	 * 
	 */
	private Socket socket;
	/**
	 * ���췽����������ʼ���ͻ���
	 * @throws Exception 
	 */
	public Client() throws Exception{
		try {
			/*
			 * ʵ����Socket���������˽�������
			 * �Ĺ��̡�������Ҫ��������������ָ��
			 * ����˵�ַ��Ϣ
			 * ����1:����˼������IP��ַ
			 * ����2:�����ڷ���˼�����ϵķ����
			 *       Ӧ�ó���򿪵ķ���˿ڡ�
			 * ͨ��IP�����ҵ�����˼��������ͨ��
			 * �˿ڿ������ӵ������ڷ���˼������
			 * �ķ����Ӧ�ó���
			 * ����ʵ�����������ӹ��̣��������û��
			 * ��Ӧ������ʵ����Socket���׳��쳣��      
			 */
			System.out.println("�������ӷ����...");
			socket = new Socket(
				"localhost",
				8088
			);
			System.out.println("�����˽�������!");
			
		} catch (Exception e) {
			/*
			 * ��������쳣����Ҫ��¼��־������
			 * ��Ҫ��֪���󡣵������쳣��Ӧ����
			 * ���ﱻ����ʱ���Լ�����catch�н���
			 * �׳���
			 */
			throw e;
		}
	}
	/**
	 * �ͻ��˿�ʼ�����ķ���
	 */
	public void start(){
		try {
			Scanner scanner = new Scanner(System.in);
			/*
			 * Socket�ṩ������
			 * OutputStream getOutputStream()
			 * ͨ��Socket��ȡ�������д�����ֽ�
			 * ����ͨ�����緢�͸�Զ�˼������
			 * ����͵��ڷ��͸�������ˡ�
			 */
			OutputStream out 
				= socket.getOutputStream();
			
			OutputStreamWriter osw
				= new OutputStreamWriter(
					out,"UTF-8");
			
			PrintWriter pw
				= new PrintWriter(osw,true);
			
			//����ȡ�������Ϣ���߳�����
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
					System.out.println("��˵���ٶ�̫�죬����Ϣһ��...");
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
			System.out.println("�ͻ�������ʧ��!");
		}
	}
	/**
	 * ���̸߳����ȡ����˷��͹�����������Ϣ��
	 * ���������̨��
	 * @author adminitartor
	 *
	 */
	private class ServerHandler implements Runnable{
		public void run(){
			try {
				//����������
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







