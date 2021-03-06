package com.happy.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.happy.server.constant.MailConstants;
import com.happy.server.pojo.Employee;
import com.happy.server.pojo.MailLog;
import com.happy.server.service.IEmployeeService;
import com.happy.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/06/19:17
 * @Description: 邮件发送定时任务
 */
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 邮件发送定时任务
     * 10 秒执行一次
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void mailTask(){
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                // lt("tryTime", LocalDateTime.now()) 重试时间小于 当前时间重试
                .eq("status", 0).lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            // 如果重试次数超过 3 次，更新状态为投递失败，不在重试
            if(3 <= mailLog.getCount()){
                mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("status",2).eq("msgId",mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>()
                .set("count",mailLog.getCount() + 1)
                .set("updateTime",LocalDateTime.now())
                .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                .eq("msgId",mailLog.getMsgId())
            );
            Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);
            rabbitTemplate.convertAndSend(
                    MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    employee,
                    new CorrelationData(mailLog.getMsgId())
            );
        });
    }
}
