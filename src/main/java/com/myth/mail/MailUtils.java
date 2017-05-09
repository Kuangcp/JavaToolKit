package com.myth.mail;

import com.myth.reflect.Config;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * 使用QQ邮箱或者是163邮箱进行发送(html )注册地址
 * @author Myth
 * 2017年1月1日 下午7:57:32
 * 2017-05-04 10:20:33 ：
 * 	最好使用redis来存储配置信息，用户名密码什么的，这样开源也不会泄露密码
 * 	2017-05-08 20:16:00
 * 		修正了之前的错误，提高了扩展性
 */
public class MailUtils {
	private static String SendServer;
	private static String port;
	private static String username;
	private static String passcode;
	private static String title;
	private static String contents="";
	private static String Receiver;

	/**
	 * 初始化所有配置信息,类型作为参数传入
	 */
	private  static void initConfig(String type){
		// 如果是maven web项目 就要src开头的路径
		Config config = new Config("/mails.properties");
		SendServer=config.getString(type+"_sendServer");
		port = config.getString(type+"_port");
		username=config.getString(type+"_username");
		passcode = config.getString(type+"_passcode");
		InputStream is = MailUtils.class.getResourceAsStream(config.getString("html_index"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(is));
		try {
			String temp;
			while ((temp = bf.readLine())!=null){contents+=temp;}
		} catch (Exception e) {
			e.printStackTrace();
		}
		title  = config.getString("title");
		showConfig();
	}
	/**
	 * 实施测试，最好使用junit来测试
	 */
	public static void main(String[] a) {
		System.out.println("Start Sending......");
		sendByQQ("1161502665@qq.com");
//		sendBy163("1181951407@qq.com");
//		sendByOutLook("15979940275@163.com");
		System.out.println("Success!");

	}
	public static void sendBy126(String receiver){
		Receiver = receiver;
		use163Or126(receiver,"126");
	}
	public static void sendBy163(String receiver){
		Receiver = receiver;
		use163Or126(receiver,"163");
	}
	public static void sendByQQ(String receiver){
		Receiver = receiver;
		sendByQQOrOutLook(receiver,"qq");
	}
	public static void sendByOutLook(String receiver){
		Receiver = receiver;
		sendByQQOrOutLook(receiver,"outlook");
	}

	/*
	 * 使用QQ邮箱或者Outlook发送邮件的方法
	 * 特点是 使用了SSL加密
	 */
	private static void sendByQQOrOutLook(String receiver,String type) {
		initConfig(type);
		Properties props = new Properties();// 1,获得Session对象
		props.setProperty("mail.smtp.host", SendServer);// props.setProperty("mail.host", "localhost");
		props.setProperty("mail.smtp.auth", "true");// 发送服务需要身份验证
		props.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议名称
		props.setProperty("mail.smtp.port", port);	// 设置端口
		// props.put("mail.debug", "true");//便于调试
		//SSL加密
		MailSSLSocketFactory sf;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.starttls.enable","true");//要加上这个属性配置，不然SSL验证通不过
			props.put("mail.smtp.ssl.socketFactory", sf);
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		}
		// Session session = Session.getDefaultInstance(props);
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, passcode);// 邮箱和第三方登录授权码
			}
		});
		createMessage(session,username,receiver,title,contents);
	}
	/**
	 * 使用163邮箱或者126来发送
	 * 密码是 客户端授权码
	 * 特点是 没有使用SSL加密
	 */
	private static void use163Or126(String receiver,String type){
		initConfig(type);
		Properties props = new Properties();
		props.setProperty("mail.host", SendServer);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.port", port);

		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, passcode);// 邮箱和第三方登录授权码
			}
		});
		createMessage(session,username,receiver,title,contents);
	}
	/**
	 * 验证Session，进行发送信息
	 */
	private static void createMessage(Session session,String username,String receiver,String title,String contents){
		Message message = new MimeMessage(session);// 2， 创建代表邮件的对象Message
		try {
			message.setFrom(new InternetAddress(username));// 设置发件人
			message.addRecipient(RecipientType.TO, new InternetAddress(receiver));			// 设置收件人
			message.setSubject(title);// 设置标题
			message.setSentDate(new Date());// 设置发送时间
			// 设置正文(有链接选择text/html;charset=utf-8)
			message.setContent(contents, "text/html;charset=utf-8");
			Transport.send(message);// 3，发送邮件Transport
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//展示发送的信息
	private static void showConfig(){
		System.out.println("Server = "+SendServer+
				"\nPort = "+port+
				"\nname = "+username+
				"\npass = "+passcode+
				"\nreceiver = "+Receiver+
				"\ntitle = "+title+
				"\ncontent = "+contents
			);
	}
}
