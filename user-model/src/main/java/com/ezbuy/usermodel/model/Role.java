package com.ezbuy.usermodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("role_id")
    private Long roleId;

    @Column(unique = true, nullable = false, length = 50)
    @JsonProperty("role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
