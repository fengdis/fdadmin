package com.fengdis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动程序
 * 
 * @author fengdis
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class FdadminApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        SpringApplication application = new SpringApplication(FdadminApplication.class);
        application.run(args);
        // System.setProperty("spring.devtools.restart.enabled", "false");
        //SpringApplication.run(fengdisApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(FdadminApplication.class);
    }
}