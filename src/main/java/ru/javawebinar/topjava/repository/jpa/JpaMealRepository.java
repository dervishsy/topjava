package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        if (meal.isNew()) {
            meal.setUser(em.getReference(User.class, userId));
            em.persist(meal);
            return meal;
        } else {
            if ((get(meal.id(),userId))==null) {
                return null;
            }
            meal.setUser(em.getReference(User.class, userId));
            return em.merge(meal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {

        Meal result = null;
        try {
            result = em.createNamedQuery(Meal.GET, Meal.class)
                    .setParameter("id", id)
                    .setParameter("user_id", userId)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        return result;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.GETBETWEENHALFOPEN, Meal.class)
                .setParameter("user_id", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}