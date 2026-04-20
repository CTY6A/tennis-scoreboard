package com.stubedavd.repository;

import com.stubedavd.entity.Match;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match> {

    Long findTotalCount();

    Long findCountByName(String playerName);

    List<Match> findPage(int pageNumber, int pageSize);

    List<Match> findByPlayerName(String name, int pageNumber, int pageSize);
}
