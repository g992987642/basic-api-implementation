package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;



@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VoteServiceImpl implements VoteService {

    private final UserRepository userRepository;
    private final RsEventRepository rsEventRepository;
    private final VotesRepository votesRepository;


    @Override
    public List<VoteEntity> getVotesBetwenStartAndEnd(long start, long end) {
        Timestamp startTimestamp = new Timestamp(start);
        Timestamp endTimestamp = new Timestamp(end);
        List<VoteEntity> votesList = votesRepository.findByVoteTimeBetween(startTimestamp, endTimestamp);
        return votesList;
    }

}
