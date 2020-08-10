package com.assignment.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


@Entity
public class Player extends AbstractEntity {

    @ManyToOne
    private Team team;

    @NotNull(message = "player.name.should.not.empty")
    private String name;


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
