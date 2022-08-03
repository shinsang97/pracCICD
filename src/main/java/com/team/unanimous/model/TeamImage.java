package com.team.unanimous.model;

import com.team.unanimous.dto.ImageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class TeamImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamImageId;

    @Column(nullable = false)
    private String filename;

    @Column
    private String teamImageUrl;

    public TeamImage(ImageDto imageDto) {
        this.filename = imageDto.getFileName();
        this.teamImageUrl = imageDto.getImageUrl();
    }
}