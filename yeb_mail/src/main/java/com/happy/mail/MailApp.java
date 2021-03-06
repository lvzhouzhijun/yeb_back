package com.happy.mail;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/06/13:53
 * @Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MailApp {

    public static void main(String[] args) {
        SpringApplication.run(MailApp.class,args);
    }

    @Bean
    public Queue queue(){
       return new Queue("mail.welcome");
    }

}
