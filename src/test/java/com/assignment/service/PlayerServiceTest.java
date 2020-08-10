package com.assignment.service;

import com.assignment.entity.Player;
import com.assignment.entity.Team;
import com.assignment.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    public static final long TEAM_ID = 1L;
    @InjectMocks
    private PlayerService service;

    @Mock
    private PlayerRepository repository;
    @Mock
    private GetTeamService teamService;

    @Test
    void canAddNewPlayer() {
        when(teamService.getById(anyLong())).thenReturn(new Team());
        Player player = new Player();
        player.setName("Manchester City F.C.");
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);

        service.add(TEAM_ID, player);

        Mockito.verify(repository).save(captor.capture());

        Player value = captor.getValue();

        Assertions.assertNotNull(value.getTeam());
        Assertions.assertEquals(player.getName(), value.getName());
    }

    @Test
    void canGetPlayerById() {
        Player value = new Player();
        Team team = new Team();
        team.setId(1L);
        value.setTeam(team);
        when(repository.findById(anyLong())).thenReturn(Optional.of(value));

        Player player = service.getById(1L, 1L);

        assertNotNull(player);
    }

    @Test
    void throwExceptionWhenGetByInvalidTeamId() {
        Player value = new Player();
        Team team = new Team();
        team.setId(2L);
        value.setTeam(team);
        when(repository.findById(anyLong())).thenReturn(Optional.of(value));

        Assertions.assertThrows(RuntimeException.class, () -> service.getById(1L, 1L));

    }

    @Test
    void throwExceptionWhenGetByInvalidId() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> service.getById(1L, null));

    }

    @Test
    void canDeletePlayerById() {
        Mockito.doNothing().when(repository).deleteById(anyLong());

        service.delete(1L, 1L);

        Mockito.verify(repository).deleteById(anyLong());

    }

    @Test
    void canUpdatePlayers() {

        Player player = new Player();
        String playerName = "test";
        Team team = new Team();
        team.setId(1L);
        player.setTeam(team);
        player.setName(playerName);

        when(repository.findById(anyLong())).thenReturn(Optional.of(player));
        when(repository.save(any(Player.class))).thenReturn(player);

        Player update = service.update(1L, 1L, player);

        assertEquals(playerName, update.getName());

        Mockito.verify(repository).save(any(Player.class));

    }

    @Test
    void canReturnPlayers() {
        when(repository.findAllByTeam(new Team(), Pageable.unpaged())).thenReturn(new PageImpl<>(singletonList(new Player())));
        when(teamService.getById(anyLong())).thenReturn(new Team());
        Page<Player> page = service.findAllPlayerByTeamId(1L, Pageable.unpaged());
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertEquals(1, page.getTotalElements());

    }

}
