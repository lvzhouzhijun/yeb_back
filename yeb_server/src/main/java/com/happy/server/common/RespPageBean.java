package com.happy.server.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/04/19:48
 * @Description: 分页公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 数据集合
     */
    private List<?> data;

}
