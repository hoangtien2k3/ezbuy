package com.ezbuy.usermodel.model;

import com.ezbuy.usermodel.model.enums.UserGender;
import com.ezbuy.usermodel.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("user_id")
    private Long userId; // ma nguoi dung

    @Column(unique = true, nullable = false, length = 50)
    private String username; // ten dang nhap

    @Column(unique = true, nullable = false, length = 100)
    private String email; // email

    @Column(nullable = false)
    private String password; // mat khau

    @JsonProperty("first_name")
    @Column(length = 50)
    private String firstName; // ten dau

    @JsonProperty("last_name")
    @Column(length = 50)
    private String lastName; // ten cuoi

    @JsonProperty("full_name")
    @Column(length = 100)
    private String fullName; // ten day du

    @Column(length = 15)
    @JsonProperty("phone_number")
    private String phoneNumber; // so dien thoai

    @Column(length = 10)
    private UserGender gender; // gioi tinh

    @JsonProperty("date_of_birth")
    private Date dateOfBirth; // ngay sinh

    @JsonProperty("profile_picture")
    private String profilePicture; // anh dai dien

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE; // trang thai

    @JsonProperty("is_verified")
    @Column(length = 20, nullable = false)
    private String postalCode; // ma buu chinh

    @Column(length = 100, nullable = false)
    private String country; // quoc gia

    @JsonProperty("created_at")
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // thoi gian tao

    @JsonProperty("updated_at")
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // thoi gian cap nhat

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses; // dia chi

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserActivity> activities; // hoat dong

    @JsonProperty("login_histories")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LoginHistory> loginHistories; // lich su dang nhap

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSetting> settings; // cau hinh

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles; // vai tro
}
