package com.assignment.rest;

import com.assignment.entity.Player;
import com.assignment.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PlayerController.API_V1_TEAMS_ID_PLAYERS)
public class PlayerController {

    public static final String API_V1_TEAMS_ID_PLAYERS = "/api/v1/teams/{id}/players";

    @Autowired
    private  PlayerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Player add(@PathVariable("id") Long teamId, @Validated @RequestBody Player player) {
        return service.add(teamId, player);
    }

    @GetMapping
    public Page<Player> findAllPlayerByTeamId(@PathVariable("id") Long teamId, @PageableDefault(size = 20, value = 0) Pageable page) {
        return service.findAllPlayerByTeamId(teamId, page);
    }

    @GetMapping("/{playerId}")
    public Player get(@PathVariable("id") Long teamId, @PathVariable("playerId") Long id) {
        return service.getById(teamId,id);
    }

    @PutMapping("/{playerId}")
    public Player update(@PathVariable("id") Long teamId, @PathVariable("playerId") Long id,@Validated @RequestBody Player player) {
        return service.update(teamId,id, player);
    }

    @DeleteMapping("/{playerId}")
    public void delete(@PathVariable("id") Long teamId, @PathVariable("playerId") Long id) {
        service.delete(teamId,id);
    }

}
