package com.stubedavd.util;

import com.stubedavd.repository.impl.HibernateMatchRepository;

//TODO: delete this
public class Main {
    public static void main(String[] args) {
        HibernateMatchRepository matchRepository = new HibernateMatchRepository();

        System.out.println(matchRepository.findCountByName("Rafael Nadal"));
        System.out.println(matchRepository.findCountByName("Andy Murray"));
    }
}
