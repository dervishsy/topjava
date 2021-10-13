package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryInMemory implements Repository<Meal, Integer> {
    private final AtomicInteger idCounter;

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    public MealRepositoryInMemory() {
        for (Meal meal : MealsUtil.meals) {
            this.save(meal);
        }

        Optional<Integer> maxId = this.meals.keySet().stream().reduce(Integer::max);
        this.idCounter = new AtomicInteger(maxId.orElse(0));
    }

    public Meal deleteById(Integer id) {
        return this.meals.remove(id);
    }

    public Meal save(Meal entity) {
        if (entity == null) {
            return null;
        }
        Integer id = entity.getId();
        if (id == null) {
            id = this.idCounter.incrementAndGet();
        }

        Meal newMeal = new Meal(id, entity.getDateTime(), entity.getDescription(), entity.getCalories());
        this.meals.put(id, newMeal);
        return newMeal;
    }

    public Collection<Meal> findAll() {
        return this.meals.values();
    }

    public Meal findById(Integer id) {
        return id == null ? null : this.meals.get(id);
    }
}
