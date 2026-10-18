package com.qlcc.dto;

import com.qlcc.enums.BillType;
import com.qlcc.validation.ApartmentExists;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BillDTO {
    @NotNull(message = "Số căn hộ không được để trống")
    @ApartmentExists(message = "Số căn hộ không tồn tại trong hệ thống")
    private String apartmentNumber;

    @NotNull(message = "Loại hóa đơn không được để trống")
    @Enumerated(EnumType.STRING)
    private BillType billType;

    @NotNull(message = "Nội dung hóa đơn không được để trống")
    private String description;

    @NotNull(message = "Số tền phải đóng không được để trống")
    @Min(value = 1, message = "Số tiền phải đóng phải lớn hơn 0")
    private Double amount;

    @NotNull(message = "Hạn đóng không được để trống")
    @Future(message = "Ngày hạn đóng phải là ngày trong tương lai")
    private LocalDate dueDate;
}
