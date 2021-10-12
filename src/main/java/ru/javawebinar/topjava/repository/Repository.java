
package ru.javawebinar.topjava.repository;

import java.util.Collection;

public interface Repository<T, ID> {
    T deleteById(ID var1);

    T save(T var1);

    Collection<T> findAll();

    T findById(ID var1);
}
