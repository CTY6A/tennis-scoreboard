package com.stubedavd.repository;

import java.util.List;

public interface CrudRepository<T> {

    T save(T t);

    List<T> findAll();
}
