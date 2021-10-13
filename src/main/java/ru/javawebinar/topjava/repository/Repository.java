
package ru.javawebinar.topjava.repository;

import java.util.Collection;

public interface Repository<T, ID> {
    T deleteById(ID id);

    T save(T id);

    Collection<T> findAll();

    T findById(ID id);
}
