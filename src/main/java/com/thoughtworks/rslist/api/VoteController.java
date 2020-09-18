package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VotesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;


@RestController
public class VoteController {
    private final VotesRepository votesRepository;

    public VoteController(VotesRepository votesRepository) {
        this.votesRepository = votesRepository;
    }
    @GetMapping("/votes")
    public ResponseEntity getVotesBetwenStartAndEnd(@RequestParam long start, @RequestParam long end){
        Timestamp startTimestamp=new Timestamp(start);
        Timestamp endTimestamp=new Timestamp(end);
        //List<VoteEntity> votesList = votesRepository.findAllByVoteTimeBetween(startDate, endDate);
        List<VoteEntity> votesList = votesRepository.findByVoteTimeBetween(startTimestamp, endTimestamp);
        return ResponseEntity.ok().body(votesList);
    }
}
