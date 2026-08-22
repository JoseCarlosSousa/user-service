package pt.kkosmico.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_CREATED_QUEUE = "user.created.queue";
    public static final String USER_CREATED_ROUTING_KEY = "user.created.routingKey";

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Queue userCreatedQueue() {
        return QueueBuilder.durable(USER_CREATED_QUEUE).build();
    }

    @Bean
    public Binding bindingUserCreated(Queue userCreatedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(userExchange).with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
