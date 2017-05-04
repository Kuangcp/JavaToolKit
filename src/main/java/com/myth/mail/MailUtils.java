package com.myth.mail;

import com.myth.reflect.Config;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * 使用QQ邮箱或者是163邮箱进行发送注册地址
 * @author Myth
 * 2017年1月1日 下午7:57:32
 * 2017-05-04 10:20:33 ：
 * 	最好使用redis来存储配置信息，用户名密码什么的，这样开源也不会泄露密码
 */
public class MailUtils {
	private static String SendServer="";
	private static String port = "";
	private static String username="";
	private static String passcode = "";
	private static String title = "";
	private static String contents= "";

	/**
	 * 初始化所有配置信息
	 */
	private  static void initConfig(){
		Config config = new Config("outlookmail.properties");

		SendServer=config.getString("sendServer");
		port = config.getString("port");
		username=config.getString("username");
		passcode = config.getString("passcode");
		title="欢迎使用，请激活！";
		contents = "<h2>你看到的是一封测试邮件</h2><br/>正如你所见，使用mail组件发送的";
	}
	/**
	 * 实施测试，最好使用junit来测试
	 */
	public static void main(String[] a) {
		System.out.println("开始");
//		sendMail("15979940275@163.com");

//		String recieve = "kuangchengping@outlook.com";
//		send163(recieve);

		sendByOutLook("15979940275@163.com");
		System.out.println("end");

	}

	/*
	 * 使用QQ邮箱发送邮件的方法
	 */
	public static void sendByQQ(String to) {
		initConfig();
		// 1,获得Session对象
		Properties props = new Properties();
		// props.setProperty("mail.host", "localhost");
		// 163邮箱服务器
		props.setProperty("mail.smtp.host", SendServer);
		// 发送服务需要身份验证
		props.setProperty("mail.smtp.auth", "true");
		// 发送邮件协议名称
		props.setProperty("mail.transport.protocol", "smtp");
		// 设置端口
		props.setProperty("mail.smtp.port", port);
		// 便于调试
		// props.put("mail.debug", "true");//便于调试

		//SSL加密
		MailSSLSocketFactory sf;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			//要加上这个属性配置，不然SSL验证通不过
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.ssl.socketFactory", sf);
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		}
		
		// Session session = Session.getDefaultInstance(props);
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				//用户名，密码（授权码）
				return new PasswordAuthentication("1181951407@qq.com", passcode);// 邮箱和第三方登录授权码
			}
		});
		createMessage(session,username,to,title,contents);

	}

	/**
	 * 使用163邮箱来发送
	 */
	public static void sendBy163(String to){
		initConfig();
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
		createMessage(session,username,to,title,contents);
	}

	/**
	 * 使用微软邮箱来发送
	 * @param to
	 */
	public static void sendByOutLook(String to){
		initConfig();
		Properties props = new Properties();
		props.setProperty("mail.host", SendServer);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.port", port);

		MailSSLSocketFactory sf;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.socketFactory", sf);
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		}
		Session session = Session.getDefaultInstance(props, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, passcode);// 邮箱和第三方登录授权码
			}

		});
		createMessage(session,username,to,title,contents);
	}

	/**
	 * 验证Session，进行发送信息
	 * @param session
	 * @param username
	 * @param to
	 * @param title
	 * @param contents
	 */
	public static void createMessage(Session session,String username,String to,String title,String contents){
		// 2， 创建代表邮件的对象Message
		Message message = new MimeMessage(session);
		try {
			// 设置发件人
			message.setFrom(new InternetAddress(username));
			// 设置收件人
			message.addRecipient(RecipientType.TO, new InternetAddress(to));
			// 设置标题
			message.setSubject(title);
			// 设置发送时间
			message.setSentDate(new Date());
			// 设置正文(有链接选择text/html;charset=utf-8)
			message.setContent(contents, "text/html;charset=utf-8");
			// 3，发送邮件Transport
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
