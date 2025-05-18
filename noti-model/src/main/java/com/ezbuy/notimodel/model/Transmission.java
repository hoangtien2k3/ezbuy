package com.ezbuy.notimodel.model;

import com.ezbuy.notimodel.model.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@ToString
@Table(name = "transmission")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transmission extends EntityBase implements Persistable<String> {

    @Id
    private String id;
    private String notificationId;
    private String receiver;
    private String email;
    private String channelId;
    private String state;
    private Integer status;
    private int resendCount;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
