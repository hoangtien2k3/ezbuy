package com.ezbuy.usermodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "login_history")
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("login_id")
    private Long loginId; // ma lich su dang nhap

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // nguoi dung

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("login_timestamp")
    private LocalDateTime loginTimestamp; // thoi gian dang nhap

    @Column(length = 45)
    @JsonProperty("ip_address")
    private String ipAddress; // dia chi IP

    @JsonProperty("user_agent")
    private String userAgent; // thong tin trinh duyet
}
