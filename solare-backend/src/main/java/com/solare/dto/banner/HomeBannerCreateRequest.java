package com.solare.dto.banner;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HomeBannerCreateRequest {
    @Size(max = 255)
    private String title;

    @Size(max = 500)
    private String subtitle;

    private Boolean active;

    @Max(9999)
    private Integer displayOrder;
}
