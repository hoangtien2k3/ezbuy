package com.ezbuy.authmodel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequest {

    @NotEmpty(message = "user.companyName.not.empty")
    @Size(max = 255, message = "user.companyName.over.length")
    private String companyName;

    @NotEmpty(message = "user.representative.not.empty")
    @Size(max = 255, message = "user.representative.over.length")
    private String representative;

    private String phone;

    @NotEmpty(message = "user.taxCode.not.empty")
    private String taxCode;

    @Size(max = 255, message = "user.taxDepartment.over.length")
    private String taxDepartment;

    @Past(message = "user.foundingDate.future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate foundingDate;

    @Size(max = 255, message = "user.businessType.over.length")
    private String businessType;

    @Size(max = 5, message = "user.provinceCode.over.length")
    @NotEmpty(message = "user.provinceCode.not.empty")
    private String provinceCode;

    @Size(max = 5, message = "user.districtCode.over.length")
    @NotEmpty(message = "user.districtCode.not.empty")
    private String districtCode;

    @Size(max = 5, message = "user.precinctCode.over.length")
    @NotEmpty(message = "user.precinctCode.not.empty")
    private String precinctCode;
}
