package com.ezbuy.authmodel.model;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "action_log")
public class ActionLog {
    private String id;
    private String userId;
    private String username;
    private String ip;
    private String type;
    private String userAgent;
    private LocalDateTime createAt;
}
