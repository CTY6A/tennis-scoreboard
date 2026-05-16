package com.stubedavd.match.model.service;

import com.stubedavd.match.model.domain.MatchScoreModel;

public interface FinishedMatchesPersistenceService {
    void recordMatch(MatchScoreModel matchScoreModel);
}
