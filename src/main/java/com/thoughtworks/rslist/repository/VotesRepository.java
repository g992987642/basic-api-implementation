package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VotesRepository extends CrudRepository<VoteEntity,Integer> {
    List<VoteEntity> findAll();
//    List<VotesEntity> findAllByUserid();
//    List<VotesEntity> findAllByRsEventid();
}
