package top.wytbook.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    public static final String SIMPLE_TASK_EXCHANGE = "simple_task_exchange";
    public static final String EMAIL_SEND_QUEUE = "email_send_queue";
    public static final String SEND_EMAIL_ROUTE = "email_route";
    public static final String LIKE_QUEUE = "like_queue";
    public static final String LIKE_ROUTE = "like_queue_route";
    public static final String WATCH_QUEUE = "watch_queue";
    public static final String WATCH_ROUTE = "watch_route";
    @Bean
    public DirectExchange simpleTaskExchange() {
        return ExchangeBuilder.directExchange(SIMPLE_TASK_EXCHANGE).durable(false).build();
    }

    @Bean
    public Queue emailSendQueue() {
        return QueueBuilder.nonDurable(EMAIL_SEND_QUEUE).build();
    }

    @Bean
    public Binding emailQueueBinding(@Value("#{simpleTaskExchange}") DirectExchange directExchange,
                                     @Value("#{emailSendQueue}") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(SEND_EMAIL_ROUTE);
    }

    @Bean
    public Queue likeQueue() {
        return QueueBuilder.nonDurable(LIKE_QUEUE).build();
    }

    @Bean
    public Binding likeQueueBinding(@Value("#{simpleTaskExchange}") DirectExchange exchange,
                                    @Value("#{likeQueue}") Queue likeQueue) {
        return BindingBuilder.bind(likeQueue).to(exchange).with(LIKE_ROUTE);
    }

    @Bean
    public Queue watchQueue() {
        return QueueBuilder.nonDurable(WATCH_QUEUE).build();
    }

    @Bean
    public Binding watchBind(@Value("#{simpleTaskExchange}") DirectExchange exchange,
                             @Value("#{watchQueue}") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(WATCH_ROUTE);
    }

}
