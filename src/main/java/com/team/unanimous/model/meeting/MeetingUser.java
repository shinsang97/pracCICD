package com.team.unanimous.model.meeting;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.team.unanimous.model.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MeetingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    public MeetingUser(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }
}
