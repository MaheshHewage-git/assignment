package com.assignment.rest;

import com.assignment.entity.Team;
import com.assignment.handler.ErrorMessage;
import com.assignment.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.assignment.rest.TeamController.API_V1_TEAMS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class TeamControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TeamRepository repository;

    @Test

    void givenTeamInfo_whenAddNewTeam_thenReturnSuccessResponse() {
        final Team team = new Team();
        team.setName("Barcelona");

        ResponseEntity<Team> responseEntity = restTemplate.postForEntity(API_V1_TEAMS, team, Team.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }

    @Test
    void givenEmptyTeamName_whenAddNewTeam_thenReturnBadRequest() {
        final Team team = new Team();

        ResponseEntity<ErrorMessage[]> responseEntity = restTemplate.postForEntity(API_V1_TEAMS, team, ErrorMessage[].class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorMessage[] body = responseEntity.getBody();

        assertEquals("name", body[0].getField());
        assertEquals("team.name.should.not.empty", body[0].getMessage());
    }


    @Test
    void givenExistingTeams_whenFindAllTeam_thenReturnTeams() {
        addTeam("ManChestHair United");
        addTeam("5 Blind Boys");

        ResponseEntity<RestResponsePage> responseEntity = restTemplate.getForEntity(API_V1_TEAMS, RestResponsePage.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        org.assertj.core.api.Assertions.assertThat(responseEntity.getBody().getContent())
            .hasSize(2)
            .extracting("name")
            .contains("ManChestHair United", "5 Blind Boys");

    }

    @Test
    void givenExistingTeams_whenFindByTeamId_thenReturnTeam() {
        Team team = addTeam("ManChestHair United");

        ResponseEntity<Team> responseEntity = findById(team.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void givenInvalidTeamId_whenFindByTeamId_thenReturnBadRequest() {

        ResponseEntity<Team> responseEntity = findById(0L);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    private ResponseEntity<Team> findById(long l) {
        return restTemplate.getForEntity(API_V1_TEAMS + "/{id}", Team.class, l);
    }

    @Test
    void givenExistingTeams_whenUpdateTeamInfo_thenReturnUpdateTeam() {
        final Team team = addTeam("ManChestHair United");

        final String newTeamName = "Manchester City F.C.";
        team.setName(newTeamName);

        restTemplate.put(API_V1_TEAMS + "/{id}", team, team.getId());

        ResponseEntity<Team> updatedTeam = findById(team.getId());

        assertEquals(HttpStatus.OK, updatedTeam.getStatusCode());
        assertEquals(newTeamName, updatedTeam.getBody().getName());

    }

    @Test
    void givenExistingTeams_whenDeleteTeamById_thenTeamShouldBeDeleted() {
        final Team team = addTeam("ManChestHair United");


        restTemplate.delete(API_V1_TEAMS + "/{id}", team.getId());

        ResponseEntity<Team> updatedTeam = findById(team.getId());

        assertEquals(HttpStatus.BAD_REQUEST, updatedTeam.getStatusCode());

    }


    private Team addTeam(String teamName) {
        Team team = new Team();
        team.setName(teamName);
        return restTemplate.postForEntity(API_V1_TEAMS, team, Team.class).getBody();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}
