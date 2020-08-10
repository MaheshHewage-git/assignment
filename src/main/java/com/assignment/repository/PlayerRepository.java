package com.assignment.repository;

import com.assignment.entity.Player;
import com.assignment.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Page<Player> findAllByTeam(Team team, Pageable pageable);

    void deleteAllByTeam(Team team);
}
