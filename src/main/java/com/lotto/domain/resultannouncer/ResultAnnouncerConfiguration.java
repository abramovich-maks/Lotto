package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultchecker.ResultCheckerFacade;

class ResultAnnouncerConfiguration {

    public static ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade, AnnouncerRepository repository) {
        return new ResultAnnouncerFacade(resultCheckerFacade, repository);
    }
}
