package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.service.VoteService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VoteController {

    private final VoteService voteService;

    @GetMapping("/votes")
    public ResponseEntity getVotesBetwenStartAndEnd(@RequestParam long start, @RequestParam long end){
        List<VoteEntity> votesList = voteService.getVotesBetwenStartAndEnd(start, end);
        return ResponseEntity.ok().body(votesList);
    }
}
