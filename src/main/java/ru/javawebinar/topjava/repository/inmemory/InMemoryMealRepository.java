package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    //    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> this.save(m, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            getUserMeals(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return getUserMeals(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        return getUserMeals(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        return getUserMeals(userId).get(id);
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return getUserMeals(userId).values().stream()
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getUserMeals(Integer userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            userMeals = new ConcurrentHashMap<>();
            repository.put(SecurityUtil.authUserId(), userMeals);
        }
        return userMeals;
    }

}

