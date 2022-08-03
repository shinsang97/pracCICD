package com.team.unanimous.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.team.unanimous.model.Image;
import com.team.unanimous.model.chat.ChatRoomUser;
import com.team.unanimous.model.meeting.MeetingUser;
import com.team.unanimous.model.team.TeamUser;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column
    private String nickname;

    @JsonIgnore
    @Column
    private String password;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TeamUser> teamList;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<MeetingUser> meeting;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<ChatRoomUser> chatRoom;

    @Column
    private boolean isGoogle;

    @OneToOne
    @JoinColumn(name = "ImageId")
    private Image image;

    @Column
    private int count;


    public User(String username, String password, boolean isGoogle, String nickname, Image image) {
        this.username = username;
        this.password = password;
        this.isGoogle = isGoogle;
        this.nickname = nickname;
        this.image = image;
    }
    public User(String password) {
        this.password = password;
    }

    //카카오 회원가입 + 구글 회원가입
    @Builder
    public User( String username, String nickname, String password, Image image, boolean isGoogle) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.image = image;
        this.isGoogle = isGoogle;
    }
    public void updateImage(Image image){
        this.image = image;
    }
}
