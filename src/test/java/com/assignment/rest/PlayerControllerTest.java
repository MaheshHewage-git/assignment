package com.assignment.rest;

import com.assignment.entity.Player;
import com.assignment.entity.Team;
import com.assignment.handler.ErrorMessage;
import com.assignment.repository.PlayerRepository;
import com.assignment.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.assignment.rest.PlayerController.API_V1_TEAMS_ID_PLAYERS;
import static com.assignment.rest.TeamController.API_V1_TEAMS;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class PlayerControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void givenPlayerInfo_whenAddNewPlayerToTeam_thenReturnSuccessResponse() {
        final Team team = addTeam();
        Player player = new Player();
        String playerName = "Paul Pogba";
        player.setName(playerName);

        ResponseEntity<Player> responseEntity = addPlayer(team, "Paul Pogba");

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(playerName, responseEntity.getBody().getName());

    }

    @Test
    void givenEmptyPlayerName_whenAddNewPlayerToTeam_thenReturnBadRequest() {
        final Team team = addTeam();
        Player player = new Player();

        ResponseEntity<ErrorMessage[]> responseEntity = restTemplate.postForEntity(API_V1_TEAMS_ID_PLAYERS,
            player,
            ErrorMessage[].class, team.getId());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorMessage[] body = responseEntity.getBody();
        assertEquals("name", body[0].getField());
        assertEquals("player.name.should.not.empty", body[0].getMessage());


    }

    @Test
    void givenMultiplePlayers_whenFindByTeamId_thenReturnAllPlayerUnderThatTeam() {
        final Team team = addTeam();
        addPlayer(team, "Paul Pogba");
        addPlayer(team, "Bruno Fernandes");
        addPlayer(team, "Mason Greenwood");

        ResponseEntity<RestResponsePage> responseEntity = restTemplate.getForEntity(API_V1_TEAMS_ID_PLAYERS,
            RestResponsePage.class, team.getId());

        assertEquals(OK, responseEntity.getStatusCode());

        org.assertj.core.api.Assertions.assertThat(responseEntity.getBody().getContent())
            .hasSize(3)
            .extracting("name")
            .contains("Paul Pogba", "Bruno Fernandes", "Mason Greenwood");


    }

    @Test
    void givenExistingPlayers_whenFindByPlayerId_thenReturnPlayer() {
        final Team team = addTeam();
        String playerName = "Paul Pogba";
        ResponseEntity<Player> responseEntity = addPlayer(team, playerName);

        ResponseEntity<Player> responseById = findById(team, responseEntity.getBody());

        assertEquals(OK, responseById.getStatusCode());
        assertEquals(playerName, responseById.getBody().getName());

    }

    @Test
    void givenExistingPlayer_whenUpdatePlayerInfo_thenPlayerShouldBeUpdated() {
        final Team team = addTeam();
        ResponseEntity<Player> responseEntity = addPlayer(team, "Paul Pogba");

        Player player = responseEntity.getBody();
        String playerName = "Bruno Fernandes";
        player.setName(playerName);

        restTemplate.put(API_V1_TEAMS_ID_PLAYERS + "/{playerId}", player, team.getId(), player.getId());

        ResponseEntity<Player> responseById = findById(team, player);

        assertEquals(OK, responseById.getStatusCode());
        assertEquals(playerName, responseById.getBody().getName());

    }

    @Test
    void givenExistingPlayers_whenDeletePlayerById_thenPlayerShouldBeDeleted() {
        final Team team = addTeam();
        ResponseEntity<Player> responseEntity = addPlayer(team, "Paul Pogba");

        restTemplate.delete(API_V1_TEAMS_ID_PLAYERS + "/{playersId}", team.getId(), responseEntity.getBody().getId());

        ResponseEntity<Player> updatedTeam = findById(team, responseEntity.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, updatedTeam.getStatusCode());

    }

    private ResponseEntity<Player> findById(Team team, Player body) {
        return restTemplate.getForEntity(API_V1_TEAMS_ID_PLAYERS + "/{playerId}", Player.class, team.getId(), body.getId());
    }

    private ResponseEntity<Player> addPlayer(Team team, String playerName) {
        Player player = new Player();
        player.setName(playerName);

        return restTemplate.postForEntity(API_V1_TEAMS_ID_PLAYERS,
            player,
            Player.class, team.getId());
    }

    private Team addTeam() {
        Team team = new Team();
        team.setName("ManChestHair United");
        return restTemplate.postForEntity(API_V1_TEAMS, team, Team.class).getBody();
    }

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
        teamRepository.deleteAll();
    }
}
