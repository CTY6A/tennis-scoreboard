package com.stubedavd.match.model.repository;

import com.stubedavd.match.model.entity.Match;

import java.util.List;

public interface MatchRepository {
    long count();
    long countByPlayerName(String playerName);
    List<Match> findAll(int pageNumber, int pageSize);
    List<Match> findAllByPlayerName(String name, int pageNumber, int pageSize);
    Match save(Match match);
}
