package util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author A-pZ
 *
 */
public class SimpleMailSender {

	/** SMTPサーバ接続ポート：25番を指定 */
	private final String SMTP_PORT = "25";

	/** SMTPメールホスト */
	private static final String smtpHost = "127.0.0.1";

	private static final String AUTH_USER_NAME = "marble.anser@gmail.com";
	private static final String AUTH_PASSWORD = "18fdewaz";

	public static void main(String args[]) throws Exception {
		SimpleMailSender mail = new SimpleMailSender();

		mail.sendMessage("webmaster@marbleanser.com", "webmaster@marbleanser.com",
				"USERNAME", "件名", "メッセージ本文です");
	}

	/**
	 * メールを送信する
	 * @param toAddr 送信先メールアドレス。
	 * @param fromAddr 送信元メールアドレス。
	 * @param personName 送信者名
	 * @param subject メールの件名
	 * @param message メール本文
	 * @throws Exception
	 */
	public void sendMessage(String toAddr, String fromAddr, String personName,
			String subject, String message) throws Exception {

		// メール送信プロパティの作成
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost); // SMTPサーバ名
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.host", smtpHost);
		props.put("mail.from", fromAddr);
		// SMTP認証設定
		props.put("mail.smtp.auth", "true");

		// SMTPソケットポート
		props.put( "mail.smtp.socketFactory.port", SMTP_PORT );

		// フォールバック
		props.put( "mail.smtp.socketFactory.fallback", String.valueOf( false ) );

		// メールセッションを確立する
		// セッション確立設定はpropsに格納される。
		Session session = Session.getDefaultInstance(props,new Authenticator()
		{
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return ( new PasswordAuthentication( AUTH_USER_NAME, AUTH_PASSWORD ) );
            }
        });
		session.setDebug(true);

		// 送信メッセージを生成
		MimeMessage mimeMsg = new MimeMessage(session);

		// 送信先（TOのほか、CCやBCCも設定可能）
		mimeMsg.setRecipients(Message.RecipientType.TO, toAddr);
		// Fromヘッダ
		InternetAddress fromHeader = new InternetAddress(fromAddr, personName);
		mimeMsg.setFrom(fromHeader);
		// 件名
		mimeMsg.setSubject(subject, "ISO-2022-JP");
		// 送信時間
		mimeMsg.setSentDate(new Date());
		// 本文
		mimeMsg.setText(message, "ISO-2022-JP");

		// メールを送信する
		Transport.send(mimeMsg);
	}
}
