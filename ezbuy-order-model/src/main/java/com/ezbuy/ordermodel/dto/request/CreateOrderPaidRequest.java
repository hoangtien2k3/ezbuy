package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.*;
import com.ezbuy.ordermodel.dto.pricing.ProductOrderItem;
import com.ezbuy.ordermodel.dto.ws.ProductOrderItemWsDTO;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CreateOrderPaidRequest {
    private String companyName;
    private String businessLicense;
    private String taxCode;
    private String companyIssueDate; // Ngay cap
    private String companyIssueBy; // noi cap
    private String companyProvince;
    private String companyDistrict;
    private String companyPrecinct;
    private String companyDetailAddress;
    private String foundingDate;
    private String companyEmail;
    private String companyPhone;
    private String customerDocument;
    private String customerIdNo;
    private String customerName;
    private String birthday;
    private String sex;
    private String customerIssueDate;
    private String customerIssuedBy;
    private String customerPhone;
    private String customerEmail;
    private String customerProvince;
    private String customerDistrict;
    private String customerPrecinct;
    private String customerDetailAddress;
    private String expiry;
    private Double totalFee;
    private String templateId;
    private String templateCode;
    private Long serviceId;
    private String serviceAlias; // bo sung alias cho PYCXXX/LuongToanTrinhScontract
    private String serviceName;
    private Boolean custAcceptAIResult;
    private Boolean checkByAI;
    private List<OrderFileDTO> customerFile;
    private String contactName;
    private String custInfoPosition;
    private String cancelUrl;
    private String returnUrl;
    private String organizationId;
    private String orderType;
    private String national;
    private String typeCustomer;
    private String fromStaff; // nhan vien moi gioi
    private String amStaffSupport; // nhan vien am ho tro
    private String subscriberEmail; // email nhan tai khoan
    private String circularsType; // loai thong tu
    private String hashPwd; // thong tin pw ma hoa
    private String userName; // thong tin username
    private String contactPhone; // thong tin so dien thoai nguoi lien he
    private String contactEmail; // thong tin email nguoi lien he
    // chinh sach xu ly va bao ve du lieu
    Map<String, String> dataPolicy;
    private String telecomServiceAlias; // aslias theo dich vu
    private InvoiceInfoDTO invoiceInfoDTO; // thong tin xuat hoa don
    private RecipientInfoDTO recipientInfoDTO; // thong tin nguoi nhan ban giao
    private String recipientInfoPosition; // chuc vu nguoi dai dien
    private Boolean isCombo; // bien kiem tra dau combo
    private Boolean isBusinessAuth; // bien kiem tra luong xac thuc doanh nghiep
    private List<ServiceItemDTO> lstTelecomService; // danh sach telecomService
    private List<ProductOrderItemWsDTO> productOrderItem; // danh sach telecomServiceAlias
    private String comboPackageName; // ten goi combo
    private Boolean isSingle; // bien kiem tra luong ddon le tai su dung luong combo
    private List<ProductOrderItem> lstProductOrderItemPricing; // danh sach productOrderItemPricing da duoc tinh gia
    private Long issueCompany; // ngay thanh lap doanh nghiep lay tu thong tin bang tenant_identify
    private List<OrderFileDTO> subscriberFile; // ho so thue bao
    private List<OrderFileDTO> serviceFile; // ho so dich vu
    private Boolean revenueForAm; // len cong no no cho AM
    private Boolean isElectronicRecords; // co phan biet ho so dien tu
    private String licenseKey; // bo sung licenseKey
    private List<PartnerLicenseKeyDTO> lstLicenseKey; // bo sung licenseKey
}
