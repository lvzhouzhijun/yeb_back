package com.happy.server;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/02/25/17:45
 * @Description:
 */
@MapperScan("com.happy.server.mapper")
@SpringBootApplication
public class YebApp {

    public static void main(String[] args) {
        SpringApplication.run(YebApp.class,args);
    }

}
