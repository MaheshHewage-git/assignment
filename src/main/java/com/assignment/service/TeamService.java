package com.assignment.service;

import com.assignment.entity.Team;
import com.assignment.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TeamService implements GetTeamService {

    private final TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    public Team add(Team team) {
        team.setPlayers(Collections.emptySet());
        return repository.save(team);
    }

    @Override
    public Team getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("invalid.id"));
    }

    public Page<Team> findAll(Pageable page) {
        return repository.findAll(page);
    }

    public Team update(Long id, Team team) {
        Team currentTeam = getById(id);
        currentTeam.setName(team.getName());
        return repository.save(currentTeam);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
