package com.ezbuy.authmodel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

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

    public void setPrecinctCode(@NonNull String precinctCode) {
        this.precinctCode = precinctCode.trim();
    }

    public void setDistrictCode(@NonNull String districtCode) {
        this.districtCode = districtCode.trim();
    }

    public void setProvinceCode(@NonNull String provinceCode) {
        this.provinceCode = provinceCode.trim();
    }

    public void setBusinessType(@NonNull String businessType) {
        this.businessType = businessType.trim();
    }

    public void setTaxDepartment(@NonNull String taxDepartment) {
        this.taxDepartment = taxDepartment.trim();
    }

    public void setRepresentative(@NonNull String representative) {
        this.representative = representative.trim();
    }

    public void setCompanyName(@NonNull String companyName) {
        this.companyName = companyName.trim();
    }


}
