package top.wytbook.util;

import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.wytbook.config.MqConfig;

import java.io.Serializable;

@Component
public class EmailUtils {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Data
    public static class EmailSendData implements Serializable {
        String target;
        String message;
        String title;
    }
    public void sendEmail(String targetEmail, String title, String message){
        EmailSendData data = new EmailSendData();
        data.setTarget(targetEmail);
        data.setMessage(message);
        data.setTitle(title);
        rabbitTemplate.convertAndSend(MqConfig.SIMPLE_TASK_EXCHANGE, MqConfig.SEND_EMAIL_ROUTE, data);
    }
}
