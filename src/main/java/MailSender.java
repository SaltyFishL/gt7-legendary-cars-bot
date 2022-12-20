import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * @author lujunqi
 * @version 1.0
 * @date 2022/12/20 09:27
 */
public class MailSender {
    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String password;
    private final String ssl;
    private final String enableStarttls;
    private final String auth;
    private final String socketFactoryClass;
    private final String protocol;
    private final String toEmail;

    public MailSender() {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        smtpHost = propertiesUtil.getProperty("mail.smtp.host");
        smtpPort = Integer.parseInt(propertiesUtil.getProperty("mail.smtp.port"));
        ssl = propertiesUtil.getProperty("mail.smtp.ssl");
        enableStarttls = propertiesUtil.getProperty("mail.smtp.starttls.enable");
        auth = propertiesUtil.getProperty("mail.smtp.auth");
        username = propertiesUtil.getProperty("mail.smtp.user");
        password = propertiesUtil.getProperty("mail.smtp.pass");
        socketFactoryClass = propertiesUtil.getProperty("mail.smtp.socketFactory.class");
        protocol = propertiesUtil.getProperty("mail.transport.protocol");
        toEmail = propertiesUtil.getProperty("to-email-address");
    }

    public void sendEmail(String subject, String content) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", protocol);
        // SMTP主机名
        props.put("mail.smtp.host", smtpHost);
        // 主机端口号
        props.put("mail.smtp.port", smtpPort);
        // 启用TLS加密
        props.put("mail.smtp.ssl", ssl);
        props.put("mail.smtp.starttls.enable", enableStarttls);
        // 是否需要用户认证
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.pass", password);
        props.setProperty("mail.smtp.socketFactory.class", socketFactoryClass);
        // 获取Session实例:
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject, "UTF-8");
        message.setText(content, "UTF-8");
        Transport.send(message);
    }
}
