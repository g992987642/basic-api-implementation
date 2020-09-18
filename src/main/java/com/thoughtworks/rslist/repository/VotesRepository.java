package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface VotesRepository extends CrudRepository<VoteEntity,Integer> {
    List<VoteEntity> findAll();



    List<VoteEntity> findByVoteTimeBetween(Timestamp start, Timestamp end);



}
