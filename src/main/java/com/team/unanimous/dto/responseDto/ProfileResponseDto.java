package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.Image;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String userImageUrl;

    public ProfileResponseDto(Image image) {

        this.userImageUrl=image.getImageUrl();
    }
}
