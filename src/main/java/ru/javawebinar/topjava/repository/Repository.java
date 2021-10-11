package ru.javawebinar.topjava.repository;


import java.util.Optional;

public interface Repository <T,ID>{
    public void deleteById(ID id);
    public T save(T entity);
    public Iterable<T>  findAll();
    public T findById(ID id);

}
