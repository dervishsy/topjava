package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class MealRepositoryImpl implements MealRepository {
    private final AtomicInteger MaxId;
    private ArrayList<Meal> meals;

    public MealRepositoryImpl() {
            this.meals = new ArrayList<>();
            this.meals.addAll(MealsUtil.getMeals());
            Optional<Integer> maxId = this.meals.stream()
                    .map(Meal::getId)
                    .reduce(Integer::max);
            MaxId = new AtomicInteger(maxId.orElse(0));
    }

    @Override
    public void deleteById(Integer id) {
        Meal currentMeal = null;
        for (Meal meal : this.meals) {
            if (id == meal.getId()) {
                currentMeal = meal;
            }
        }

        if (currentMeal != null) {
            this.meals.remove(currentMeal);
        }
    }

    @Override
    public Meal save(Meal entity) {
        Meal currentMeal = null;
        for (Meal meal : this.meals) {
            if (entity.getId() == meal.getId()) {
                currentMeal = meal;
            }
        }

        int maxId;
        if (currentMeal == null) {
            maxId = MaxId.incrementAndGet();
        } else {
            maxId = currentMeal.getId();
            deleteById(currentMeal.getId());
        }

        Meal newMeal = new Meal(maxId
                , entity.getDateTime()
                , entity.getDescription()
                , entity.getCalories());

        this.meals.add(newMeal);
        return newMeal;
    }

    @Override
    public Iterable<Meal> findAll() {
        return MealsUtil.getMeals();
    }

    public Iterable<MealTo> getMealsTo() {
        return MealsUtil.getMealsTo(this.meals);
    }


    @Override
    public Meal findById(Integer id) {
        Meal currentMeal = null;
        for (Meal meal : this.meals) {
            if (id == meal.getId()) {
                currentMeal = meal;
            }
        }

        return currentMeal;
    }
}
