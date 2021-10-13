package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepositoryInMemory;
import ru.javawebinar.topjava.repository.Repository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/mealEdit.jsp";
    private static final String LIST = "/meals.jsp";
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private Repository<Meal, Integer> dao;

    public void init() {
        this.dao = new MealRepositoryInMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action.toLowerCase(Locale.ROOT)) {
            case "delete":
                this.delete(request, response);
                break;
            case "insert":
                this.add(request, response);
                break;
            case "edit":
                this.edit(request, response);
                break;
            default:
                this.list(request, response);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals list");
        request.setAttribute("mealToList", MealsUtil.getMealsTo(this.dao.findAll(), MealsUtil.CALORIES_PER_DAY));
        this.forward(request, response, LIST);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to add meal");
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Meal meal = new Meal(null, localDateTime, "", 0);
        request.setAttribute("meal", meal);
        this.forward(request, response, INSERT_OR_EDIT);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Meal meal = dao.findById(this.getId(request));
        if (meal != null) {
            log.debug("forward to edit meal={}", meal);
            request.setAttribute("meal", meal);
            this.forward(request, response, INSERT_OR_EDIT);
        } else {
            this.add(request, response);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Meal meal = dao.deleteById(this.getId(request));
        log.debug("redirect to delete meal = {}", meal);
        response.sendRedirect("meals");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String forward) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    private Integer getId(HttpServletRequest request) {
        String idString = request.getParameter("id");
        if (idString == null || idString.isEmpty()) {
            return null;
        }
        return Integer.parseInt(idString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Integer id = this.getId(request);
        String description = request.getParameter("description");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal(id, dateTime, description, calories);
        if (id == null) {
            log.debug("save new meal={}", meal);
        } else {
            log.debug("save edited meal={}", meal);
        }
        this.dao.save(meal);

        request.setAttribute("mealToList", MealsUtil.getMealsTo(this.dao.findAll(), MealsUtil.CALORIES_PER_DAY));
        this.forward(request, response, LIST);
    }
}
