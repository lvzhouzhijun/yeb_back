package com.happy.mail;

import com.happy.server.pojo.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

      @RabbitListener(queues = "mail.welcome")
      public void handler(Employee employee){
          MimeMessage message = javaMailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message);
          try {
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
          } catch (MessagingException e) {
              LOGGER.error("邮件发送失败===========================>{}",e.getMessage());
          }
      }
}
