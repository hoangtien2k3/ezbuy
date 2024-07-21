package com.ezbuy.notification.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EntityBase implements Serializable {
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
}
