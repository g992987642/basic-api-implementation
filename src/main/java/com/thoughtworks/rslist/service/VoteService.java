package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface VoteService {
    List<VoteEntity> getVotesBetwenStartAndEnd(long start, long end);
}
