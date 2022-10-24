package si.um.feri.nanoclm.repo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import javax.jms.ConnectionFactory;
import java.util.logging.Logger;

@Configuration
public class ActiveMQConfig {

    private static final Logger log = Logger.getLogger(ActiveMQConfig.class.toString());

    @Value("${active-mq.broker-ip}")
    private String brokerIp;

    @Value("${active-mq.broker-port}")
    private String brokerPort;

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory  = new ActiveMQConnectionFactory();
        log.info("Connectiong to AMQ: "+"tcp://"+brokerIp+":"+brokerPort);
        activeMQConnectionFactory.setBrokerURL("tcp://"+brokerIp+":"+brokerPort);
        //activeMQConnectionFactory.setTrustedPackages(List.of("si.um.feri.nanoclm.repo.events","java.time"));
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setPubSubDomain(true);  //topic
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setPubSubDomain(true); //topic
        return factory;
    }

}