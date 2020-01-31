package com.itlaoqi.bsbdj.common.task;

import com.itlaoqi.bsbdj.service.CrawlerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

//组件 @Controller   /@Service  /@Respository / @Component
@Component //会被IOC容器加载
public class CrawlerTask {
    Logger logger = LoggerFactory.getLogger(CrawlerTask.class);
    //在S B运行时自动会将参数注入到urls变量中
    @Value("${app.crawler.enabled}")
    private Boolean enabled; //开启关闭爬虫
    @Value("${app.crawler.urls}")
    private String urls;//爬虫地址
    @Resource
    private CrawlerService crawlerService ;
    //任务执行的方法
    //@Scheduled 注解用于指定当前方法是一个任务调度 cron表达式指定执行的间隔
    //* * * * * ?  每秒执行一次
    //秒     分      时      日     月     星期
    //*代表任意时间    具体的数字代表精确值      ,用于多个值     /用于固定间隔    - 范围  ?忽略
    //0  * 23 * * ?     每天23点~24点之间每分钟执行一次
    //0  0 8-18  * ?    每天早上8点到下午六点，0份0秒准时执行一次
    //0  0  0  */2 * ?  0  2  4  6 ... 22  0点0分执行

    @Scheduled(cron = "${app.crawler.cron}")//每小时执行一次
    public void crawlerRunner(){
        if(enabled == false){
            logger.warn("爬虫任务已被禁止，如需启用请设置app.crawler.enabled=true");
            return;
        }
        //1. 抓取原始数据
        // URL模板
        String[] templates = urls.split(",");
        for(int i = 0 ; i < templates.length ; i ++) {
            logger.info("正在抓取第{}个模块的数据" , (i+1));
            String template = templates[i];
            Map context = new HashMap();
            context.put("count", 0);//记录抓取的总数
            crawlerService.crawl(context, template, "0" , (i+1));
        }

        //对Source表数据进行处理
        crawlerService.etl();
    }
}
