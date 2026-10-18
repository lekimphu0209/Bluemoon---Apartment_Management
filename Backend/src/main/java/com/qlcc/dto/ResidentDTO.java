package com.qlcc.dto;

import com.qlcc.enums.ResidentStatus;
import com.qlcc.validation.ApartmentsExists;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResidentDTO {
    private String fullName;
    private Long age;
    private String phone;

    @Pattern(regexp = "^(ADMIN|USER)$", message = "Role must be ADMIN or USER")
    private String role;

    @ApartmentsExists(message = "Apartment không tồn tại trong hệ thống")
    private Set<String> apartmentNumbers;

    private ResidentStatus status;
}
