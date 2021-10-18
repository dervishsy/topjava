package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        Integer userId = SecurityUtil.authUserId();
        log.info("getAll for userId {}", userId);
        return MealsUtil.getTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id) {
        Integer userId = SecurityUtil.authUserId();
        log.info("get {} for userId {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        Integer userId = SecurityUtil.authUserId();
        log.info("create {} for userId {}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        Integer userId = SecurityUtil.authUserId();
        log.info("delete {} for userId {}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        Integer userId = SecurityUtil.authUserId();
        log.info("update {} with id={} for userId {}", meal, id, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public List<MealTo> filter(LocalDate beginDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        Integer userId = SecurityUtil.authUserId();
        log.info("filter for userId {} ,beginDate {},  endDate {},  startTime {},  endTime {}"
                , userId, beginDate, endDate, startTime, endTime);
        return MealsUtil.getFilteredByDateTimeTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY
                , beginDate, endDate, startTime, endTime);
    }
}