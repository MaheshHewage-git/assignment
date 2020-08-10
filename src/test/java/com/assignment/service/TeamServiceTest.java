package com.assignment.service;

import com.assignment.entity.Team;
import com.assignment.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository repository;


    @InjectMocks
    private TeamService teamService;

    @Test
    void canAddNewTeam() {

        Team team = new Team();
        team.setName("Manchester City F.C.");
        when(repository.save(any(Team.class))).thenReturn(team);

        Team addTeam = teamService.add(team);

        assertEquals(team.getName(), addTeam.getName());

        Mockito.verify(repository).save(any(Team.class));
    }

    @Test
    void canGetTeamById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Team()));

        Team team = teamService.getById(1L);

        assertNotNull(team);
    }

    @Test
    void throwExceptionWhenGetByInvalidId() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> teamService.getById(null));

    }

    @Test
    void canDeleteTeamById() {
        Mockito.doNothing().when(repository).deleteById(anyLong());

        teamService.delete(1L);

        Mockito.verify(repository).deleteById(anyLong());

    }

    @Test
    void canUpdateTeam() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Team()));

        Team team = new Team();
        String teamName = "test";
        team.setName(teamName);

        when(repository.save(any(Team.class))).thenReturn(team);

        Team update = teamService.update(1L, team);

        assertEquals(teamName, update.getName());

        Mockito.verify(repository).save(any(Team.class));

    }

    @Test
    void canReturnTeams() {
        when(repository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(Collections.singletonList(new Team())));

        Page<Team> page = teamService.findAll(Pageable.unpaged());
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertEquals(1, page.getTotalElements());

    }
}
