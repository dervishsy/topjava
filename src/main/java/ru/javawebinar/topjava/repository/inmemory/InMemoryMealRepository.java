package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> this.save(m, SecurityUtil.USER_ID));
        MealsUtil.mealsAdmin.forEach(m -> this.save(m, SecurityUtil.ADMIN_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save userid={} meal={}", userId, meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            getUserMealsForChange(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return getUserMealsForChange(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    private Map<Integer, Meal> getUserMealsForChange(int userId) {
        return repository.computeIfAbsent(userId, u -> new ConcurrentHashMap<>());
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete userid={} mealId={}", userId, id);
        return getUserMealsForChange(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get userid={} mealId={}", userId, id);
        return getUserMeals(userId).get(id);
    }

    private Map<Integer, Meal> getUserMeals(int userId) {
        return repository.getOrDefault(userId, new HashMap<>());
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll userid={}", userId);
        return getFiltered(userId, p -> true);
    }

    @Override
    public List<Meal> getFiltered(int userId, Predicate<Meal> filter) {
        return getUserMeals(userId).values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }
}

