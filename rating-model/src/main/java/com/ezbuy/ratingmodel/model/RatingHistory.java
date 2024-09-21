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
@Table(name = "rating_type")
public class RatingHistory extends EntityBase implements Persistable<String> {
    @Id
    private String id;

    private String ratingId;
    private Long ratingBf;
    private Long ratingAf;
    private String contentBf;
    private String contentAf;
    private LocalDateTime approveAt;
    private String approveBy;
    private Rating.RatingState state;
    private Integer status;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
