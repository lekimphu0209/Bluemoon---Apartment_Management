package com.qlcc.dto;

import com.qlcc.enums.ApartmentStatus;
import com.qlcc.validation.UniqueNumber;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApartmentDTO {
    @NotBlank(message = "Số căn hộ là cần thiết")
    @UniqueNumber(message = "Số căn hộ đã tồn tại")
    private String apartmentNumber;

    @NotNull(message = "Số tầng là cần thiết")
    @Min(value = 1, message = "vui lòng nhập vào số tầng hợp lệ")
    @Max(value = 30, message = "số tầng tối đa là 30")
    private Integer floor;

    @NotNull(message = "Diện tích là cần thiết")
    @DecimalMin(value = "0.1", message = "vui lòng nhập vào diện tích hợp lệ")
    private Double area;

    @NotNull(message = "Trạng thái là cần thiết")
    private ApartmentStatus status;
}
