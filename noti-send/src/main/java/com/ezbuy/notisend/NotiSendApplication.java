package com.ezbuy.notisend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                SecurityAutoConfiguration.class
        })
@ComponentScan(basePackages = {"com.ezbuy.*"})
@ImportResource({"classpath*:web-client.xml"})
@EnableScheduling
public class NotiSendApplication {
    public static void main(String[] args) {
//        SpringApplication.run(NotiSendApplication.class, args);

        List<String> list = Arrays.asList("apple", "banana", "  cherry");

        Map<Character, Long> charCount = charCount(list);
        System.out.println(charCount);
    }

    public static Map<Character, Long> charCount(List<String> list) {
        return list.stream()
                .flatMap(str -> str.chars().mapToObj(c -> (char) c))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }

    public static Map<Character, Long> charCounts(List<String> list) {
        return list.stream()
                .map(str -> str.chars().mapToObj(c -> (char) c))  // Mỗi chuỗi biến thành một Stream<Character>
                .reduce(Stream::concat)  // Hợp nhất các Stream<Character> lại thành một Stream duy nhất
                .orElseGet(Stream::empty)  // Nếu không có chuỗi nào, trả về Stream rỗng
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));  // Nhóm và đếm ký tự
    }
}
