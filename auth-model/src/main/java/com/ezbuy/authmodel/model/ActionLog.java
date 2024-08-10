package com.ezbuy.authmodel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

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
