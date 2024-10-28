package com.ezbuy.productmodel.model;

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
@Table(name = "sync_history_detail")
@Builder
public class SyncHistoryDetail implements Persistable<String> {
    @Id
    private String id;

    private String syncTransId; // ma transId do databus tra ve de dinh danh luong dong bo
    private String syncHistoryId; // ma bang syncHistory
    private String targetId; // id cua khach hang
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
