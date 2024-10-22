package com.dataeye.partner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jaret
 * @date 2024/10/21 17:32
 * @description
 */
@MapperScan(basePackages = {"com.dataeye.partner.mapper"})
@SpringBootApplication
public class AdxPlayletAppletPartnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdxPlayletAppletPartnerApplication.class, args);
    }

}
