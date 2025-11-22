package com.ezbuy.productservice.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sync_history")
@Builder
public class SyncHistory implements Persistable<String> {
    @Id
    private String id;

    private String orgId;
    private String idNo;
    private String action;
    private String serviceType;
    private String dstService;
    private String syncType;
    private String objectType;
    private String ids;
    private String requestId;
    private String responseData;
    private LocalDateTime responseAt;
    private String errorCode;
    private String responseMessage;
    private Integer retry;
    private String state;
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
    private String syncTransId;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
