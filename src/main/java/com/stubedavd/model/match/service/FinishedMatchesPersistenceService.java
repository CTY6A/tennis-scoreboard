package com.stubedavd.model.match.service;

import com.stubedavd.model.match.domain.MatchScoreModel;

public interface FinishedMatchesPersistenceService {
    void recordMatch(MatchScoreModel matchScoreModel);
}
