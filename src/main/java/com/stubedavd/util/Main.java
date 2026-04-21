package com.stubedavd.util;

import com.stubedavd.repository.MatchRepository;

//TODO: delete this
public class Main {
    public static void main(String[] args) {
        MatchRepository matchRepository = new MatchRepository();

        System.out.println(matchRepository.findCountByName("Rafael Nadal"));
        System.out.println(matchRepository.findCountByName("Andy Murray"));
    }
}
