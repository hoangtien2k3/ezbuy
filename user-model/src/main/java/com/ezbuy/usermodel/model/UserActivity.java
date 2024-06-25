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
@Table(name = "user_activity")
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("activity_id")
    private Long activityId; // ma hoat dong

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // nguoi dung

    @Column(nullable = false, length = 50)
    @JsonProperty("activity_type")
    private String activityType; // loai hoat dong

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("activity_timestamp")
    private LocalDateTime activityTimestamp; // thoi gian hoat dong

    private String details; // chi tiet
}
