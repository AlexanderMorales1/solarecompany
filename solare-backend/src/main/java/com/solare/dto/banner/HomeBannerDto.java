package com.solare.dto.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeBannerDto {
    private Long id;
    private String imageUrl;
    private String title;
    private String subtitle;
    private boolean active;
    private int displayOrder;
}
