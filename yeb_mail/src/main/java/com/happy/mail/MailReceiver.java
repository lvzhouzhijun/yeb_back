package com.happy.mail;

import com.happy.server.constant.MailConstants;
import com.happy.server.pojo.Employee;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/06/14:04
 * @Description:
 */

@Component
public class MailReceiver {

      private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

      @Autowired
      private JavaMailSender javaMailSender;

      @Autowired
      private MailProperties mailProperties;

      @Autowired
      private TemplateEngine templateEngine;

      private RedisTemplate redisTemplate;

      @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
      public void handler(Message msg, Channel channel){
          Employee employee = (Employee) msg.getPayload();
          MessageHeaders headers = msg.getHeaders();
          // 消息序号
          long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
          String msgId = (String) headers.get("spring_returned_message_correlation");
          HashOperations hashOperations = redisTemplate.opsForHash();
          try {
              if(hashOperations.entries("mail_log").containsKey(msgId)){
                  LOGGER.error("消息已经被消费==================>",msgId);
                  channel.basicAck(tag,false);
                  return;
              }
              MimeMessage message = javaMailSender.createMimeMessage();
              MimeMessageHelper helper = new MimeMessageHelper(message);
              // 发件人
              helper.setFrom(mailProperties.getUsername());
              // 收件人
              helper.setTo(employee.getEmail());
              // 主题
              helper.setSubject("入职欢迎邮件");
              // 发送日期
              helper.setSentDate(new Date());
              // 邮件内容
              Context context = new Context();
              context.setVariable("name",employee.getName());
              context.setVariable("posName",employee.getPosition().getName());
              context.setVariable("joblevelName",employee.getJoblevel().getName());
              context.setVariable("departmentName",employee.getDepartment().getName());
              // 通过模板引擎拿到这个 mail.html 文件，把值设置进去
              String mail = templateEngine.process("mail", context);
              // true 代表HTML，会把我们的 HTML 转换成页面的
              helper.setText(mail,true);
              // 发送邮件
              javaMailSender.send(message);
              LOGGER.info("邮件发送成功");
              hashOperations.put("mail_log",msgId,"OK");
              /**
               * 手动确认
               *  tag：代表消息的序号
               *  true：是否一次性确认多条
               */
              channel.basicAck(tag,false);
          } catch (MessagingException | IOException e) {
              /**
               * 拒绝消息
               * requeue：消息是否重新回到队列
               */
              try {
                  channel.basicNack(tag,false,true);
              } catch (IOException ioException) {
                  LOGGER.error("邮件发送失败===========================>{}",e.getMessage());
              }
              LOGGER.error("邮件发送失败===========================>{}",e.getMessage());
          }
      }
}
