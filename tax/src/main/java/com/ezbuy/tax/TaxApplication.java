package com.ezbuy.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootApplication
public class TaxApplication {

    public static void main(String[] args) {
//        SpringApplication.run(TaxApplication.class, args);

        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Định dạng ngày giờ theo kiểu dd/MM/yyyy HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Chuyển thời gian thành chuỗi theo định dạng
        String formattedDateTime = now.format(formatter);

        // In ra kết quả
        System.out.println("Thời gian hiện tại: " + formattedDateTime);

    }
}
