package com.ezbuy.ratingmodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rating_count")
public class RatingCount implements Persistable<String> {
    @Id
    private String id;

    private String ratingTypeCode;
    private String targetId;
    private Long numberRate;
    private Float rating;
    private Long maxRating;
    private String detail;
    private String serviceAlias;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
