package com.ezbuy.usermodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("address_id")
    private Long addressId; // ma dia chi

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // nguoi dung

    @Column(nullable = false, length = 255)
    @JsonProperty("address_line1")
    private String addressLine1; // dia chi 1

    @Column(length = 255)
    @JsonProperty("address_line2")
    private String addressLine2; // dia chi 2

    @Column(nullable = false, length = 100)
    private String city; // thanh pho

    @Column(nullable = false, length = 100)
    private String state; // tinh

    @Column(nullable = false, length = 20)
    @JsonProperty("postal_code")
    private String postalCode; // ma buu chinh

    @Column(nullable = false, length = 100)
    private String country; // quoc gia

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("created_at")
    private LocalDateTime createdAt; // thoi gian tao

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt; // thoi gian cap nhat
}
