package top.wytbook.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import top.wytbook.config.MqConfig;
import top.wytbook.util.EmailUtils;

@Component
@RabbitListener(queues = MqConfig.EMAIL_SEND_QUEUE)
public class EmailMqHandler {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String from;

    @RabbitHandler
    public void emailHandler(EmailUtils.EmailSendData data) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setSubject(data.getTitle());
            simpleMailMessage.setText(data.getMessage());
            simpleMailMessage.setTo(data.getTarget());
            javaMailSender.send(simpleMailMessage);
        }
        catch (Exception ignored) {

        }
    }
}
