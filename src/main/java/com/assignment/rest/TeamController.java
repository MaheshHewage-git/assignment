package com.assignment.rest;

import com.assignment.entity.Team;
import com.assignment.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TeamController.API_V1_TEAMS)

public class TeamController {

    public static final String API_V1_TEAMS = "/api/v1/teams";
    @Autowired
    private TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team add(@Validated @RequestBody Team team) {
        return teamService.add(team);
    }

    @GetMapping
    public RestResponsePage<Team> findAll(@PageableDefault(size = 20, value = 0) Pageable page) {
        return RestResponsePage.of(teamService.findAll(page));
    }

    @GetMapping("/{id}")
    public Team get(@PathVariable("id") Long id) {
        return teamService.getById(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @Validated @RequestBody Team team) {
        teamService.update(id, team);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        teamService.delete(id);
    }

}
