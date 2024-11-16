package com.ezbuy.authservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        exclude = {
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            SecurityAutoConfiguration.class,
        })
@ComponentScan(basePackages = {"com.ezbuy", "com.reactify"})
@EnableScheduling
public class AuthServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
        logger.info("Start");
    }
}
