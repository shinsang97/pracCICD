package com.team.unanimous.model.team;

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
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public TeamUser(User user, Team team) {
        this.user = user;
        this.team = team;
    }
}
