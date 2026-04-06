package com.stubedavd.repository;

import java.util.List;

public interface CrudRepository<T> {

    T create(T t);

    List<T> readAll();

    T readById(Integer id);

    T update(T t);

    T delete(T t);
}
