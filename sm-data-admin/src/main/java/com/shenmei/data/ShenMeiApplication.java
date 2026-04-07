package com.shenmei.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
//@ComponentScan(basePackages = {"com.shenmei.data.sse"})
public class ShenMeiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(ShenMeiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  申美数据启动成功   ლ(´ڡ`ლ)ﾞ ");

    }
}
