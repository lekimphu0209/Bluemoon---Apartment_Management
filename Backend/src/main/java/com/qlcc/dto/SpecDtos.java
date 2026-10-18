package com.qlcc.dto;

import com.qlcc.enums.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Request DTOs cho cac endpoint theo dac ta BlueMoon (8 trang).
 * Gom chung mot file de de doc; moi class tuong ung mot form/modal.
 */
public class SpecDtos {

    // ---- Khoan thu (F-KT) ----
    @Getter @Setter
    public static class FeeTypeRequest {
        @NotBlank(message = "Vui lòng nhập tên khoản thu.")
        private String name;
        @NotNull(message = "Vui lòng chọn loại khoản thu.")
        private FeeTypeType type;
        private BigDecimal rate;
        private BigDecimal suggestedAmount;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean mandatory;
        private FeeTypeStatus status;
        private String description;
    }

    // ---- Can ho (F-CH) ----
    @Getter @Setter
    public static class ApartmentRequest {
        @NotBlank(message = "Vui lòng nhập số căn hộ.")
        private String apartmentNumber;
        @NotNull(message = "Vui lòng nhập tầng.")
        @Min(value = 1, message = "Tầng phải từ 1 đến 30.")
        @Max(value = 30, message = "Tầng phải từ 1 đến 30.")
        private Integer floor;
        @NotNull(message = "Vui lòng nhập diện tích.")
        @Positive(message = "Diện tích phải lớn hơn 0.")
        private Double area;
    }

    // ---- Nguoi dan (F-ND) ----
    @Getter @Setter
    public static class ResidentRequest {
        @NotBlank(message = "Vui lòng nhập họ và tên.")
        private String fullName;
        private LocalDate birthDate;
        private String gender;
        @NotBlank(message = "Vui lòng nhập CCCD.")
        private String idCard;
        private String phone;
        @Email(message = "Email không đúng định dạng.")
        private String email;
        @NotNull(message = "Vui lòng chọn căn hộ.")
        private Long apartmentId;
        @NotNull(message = "Vui lòng chọn quan hệ với chủ hộ.")
        private Relationship relationship;
    }

    @Getter @Setter
    public static class ResidentUpdateRequest {
        @NotBlank(message = "Vui lòng nhập họ và tên.")
        private String fullName;
        private LocalDate birthDate;
        private String gender;
        @NotBlank(message = "Vui lòng nhập CCCD.")
        private String idCard;
        private String phone;
        @Email(message = "Email không đúng định dạng.")
        private String email;
        @NotNull(message = "Vui lòng chọn quan hệ với chủ hộ.")
        private Relationship relationship;
    }

    // ---- Thu phi (F-TP) ----
    @Getter @Setter
    public static class GenerateRequest {
        @NotBlank(message = "Chọn kỳ thu.")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "Kỳ thu phải có dạng YYYY-MM.")
        private String period;
        @NotEmpty(message = "Chọn ít nhất một khoản thu.")
        private List<Long> feeTypeIds;
    }

    @Getter @Setter
    public static class CollectRequest {
        @NotNull(message = "Chọn ngày thu.")
        private LocalDate paidDate;
        private String note;
    }

    @Getter @Setter
    public static class CollectBulkRequest {
        private List<Long> ids;
        private Map<String, Object> filter;
        private LocalDate paidDate;
    }

    @Getter @Setter
    public static class ContributionRequest {
        @NotNull(message = "Chọn căn hộ.")
        private Long apartmentId;
        @NotNull(message = "Chọn khoản đóng góp.")
        private Long feeTypeId;
        @NotNull(message = "Nhập số tiền.")
        @Positive(message = "Số tiền phải lớn hơn 0.")
        private BigDecimal amount;
        @NotNull(message = "Chọn ngày thu.")
        private LocalDate paidDate;
        private String note;
    }

    @Getter @Setter
    public static class AmountRequest {
        @NotNull(message = "Nhập số tiền.")
        @Positive(message = "Số tiền phải lớn hơn 0.")
        private BigDecimal amount;
        private String note;
    }

    // ---- Bien dong (F-BD) ----
    @Getter @Setter
    public static class ResidencyEventRequest {
        @NotNull(message = "Chọn loại biến động.")
        private ResidencyEventType type;
        @NotNull(message = "Chọn người dân.")
        private Long residentId;
        private Long apartmentId;
        @NotNull(message = "Chọn ngày xảy ra.")
        private LocalDate eventDate;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String destination;
        private String note;
    }

    // ---- Phuong tien (F-CH-10, v2.0) ----
    @Getter @Setter
    public static class VehicleRequest {
        @NotNull(message = "Chọn loại xe.")
        private VehicleType type;
        @NotBlank(message = "Nhập biển số.")
        private String plate;
    }

    // ---- Tai khoan (F-TA) ----
    @Getter @Setter
    public static class MeRequest {
        @NotBlank(message = "Vui lòng nhập họ và tên.")
        private String fullName;
    }

    @Getter @Setter
    public static class PasswordRequest {
        @NotBlank(message = "Vui lòng nhập mật khẩu hiện tại.")
        private String oldPassword;
        @NotBlank(message = "Vui lòng nhập mật khẩu mới.")
        @Size(min = 8, message = "Mật khẩu mới phải có ít nhất 8 ký tự.")
        private String newPassword;
    }

    // ---- Xoa hang loat (L-06) ----
    @Getter @Setter
    public static class BulkIdsRequest {
        @NotEmpty(message = "Chưa chọn dòng nào.")
        private List<Long> ids;
    }
}
