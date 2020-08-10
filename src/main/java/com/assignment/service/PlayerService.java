package com.assignment.service;

import com.assignment.entity.Player;
import com.assignment.entity.Team;
import com.assignment.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public class PlayerService {

    private final PlayerRepository repository;
    private final GetTeamService teamService;

    public PlayerService(PlayerRepository repository, GetTeamService teamService) {
        this.repository = repository;
        this.teamService = teamService;
    }

    public Player add(Long teamId, Player player) {
        final Team team = teamService.getById(teamId);
        player.setTeam(team);
        return repository.save(player);
    }

    public Player getById(Long teamId, Long id) {
        final Player player = repository.findById(id).orElseThrow(() -> new RuntimeException("invalid.id"));
        if (!teamId.equals(player.getTeam().getId())) {
            throw new RuntimeException("invalid.team.id");
        }
        return player;
    }

    public Page<Player> findAllPlayerByTeamId(Long teamId, Pageable page) {
        return repository.findAllByTeam(teamService.getById(teamId), page);
    }

    public Player update(Long teamId, Long id, Player player) {
        Player entity = getById(teamId, id);
        entity.setName(player.getName());
        return repository.save(entity);
    }

    public void delete(Long teamId, Long id) {
        repository.deleteById(id);
    }
}
