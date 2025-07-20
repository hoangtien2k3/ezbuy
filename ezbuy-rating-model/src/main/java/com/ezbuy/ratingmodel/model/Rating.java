package com.ezbuy.ratingmodel.model;

import com.ezbuy.ratingmodel.model.base.EntityBase;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "rating")
public class Rating extends EntityBase implements Persistable<String> {

    @Id
    private String id;
    private String ratingTypeCode;
    private String targetId;
    private String username;
    private String custName;
    private Long rating;
    private String content;
    private LocalDateTime ratingDate;
    private Integer hasImage;
    private Integer hasVideo;
    private Integer status;
    private String state;
    private Integer displayStatus;
    private Integer sumRateStatus;
    private String targetUser;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
