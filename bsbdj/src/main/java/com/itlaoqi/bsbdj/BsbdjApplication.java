package com.itlaoqi.bsbdj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//扫描mapper包，自动生成mapper对应的实现类
@MapperScan("com.itlaoqi.bsbdj.mapper")
@EnableScheduling //EnableScheduling启用任务调度
public class BsbdjApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsbdjApplication.class, args);
	}
}
