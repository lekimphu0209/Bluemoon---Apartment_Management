package com.qlcc.validation;

import com.qlcc.entity.Resident;
import com.qlcc.repository.ResidentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResidentBelongsToApartmentValidator implements ConstraintValidator<ResidentBelongsToApartment, Object> {
    @Autowired
    private ResidentRepository residentRepository;

    private String apartmentNumberField;
    private String residentIdField;

    @Override
    public void initialize(ResidentBelongsToApartment constraintAnnotation) {
        this.apartmentNumberField = constraintAnnotation.apartmentNumberField();
        this.residentIdField = constraintAnnotation.residentIdField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object apartmentNumberValue = new BeanWrapperImpl(value).getPropertyValue(apartmentNumberField);
            Object residentIdValue = new BeanWrapperImpl(value).getPropertyValue(residentIdField);

            if (apartmentNumberValue == null || residentIdValue == null) {
                return false;
            }

            String apartmentNumber = apartmentNumberValue.toString();
            Long residentId = Long.valueOf(residentIdValue.toString());

            // Schema V2: kiem tra qua residents.apartment -> apartments.number
            Resident resident = residentRepository.findById(residentId).orElse(null);
            return resident != null
                    && resident.getApartment() != null
                    && apartmentNumber.equals(resident.getApartment().getApartmentNumber());
        } catch (Exception e) {
            return false;
        }
    }
}
