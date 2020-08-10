package com.assignment.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity

public class Team extends AbstractEntity {

    @NotNull(message = "team.name.should.not.empty")
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Player> players;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
