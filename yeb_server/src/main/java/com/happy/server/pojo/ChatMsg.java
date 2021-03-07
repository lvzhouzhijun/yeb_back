package com.happy.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/07/13:54
 * @Description: 消息
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ChatMsg {

    private String from;

    private String to;

    private String content;

    private LocalDateTime date;

    private String formNickName;

}
