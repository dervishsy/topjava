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
import java.util.Locale;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/mealEdit.jsp";
    private static final String LIST = "/meals.jsp";
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private Repository<Meal, Integer> dao;

    public void init() throws ServletException {
        super.init();
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Integer id = this.getId(request);
        String description = request.getParameter("description");

        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        } catch (Exception var9) {
            log.debug("Error!!Edit meal. Date error {}", request.getParameter("dateTime"));
            request.setAttribute("mealToList", MealsUtil.getMealsTo(this.dao.findAll()));
            this.forward(request, response, LIST);
            return;
        }

        int calories;
        try {
            calories = Integer.parseInt(request.getParameter("calories"));
        } catch (NumberFormatException var8) {
            log.debug("Error!!Edit meal.Calories error {}", request.getParameter("calories"));
            request.setAttribute("mealToList", MealsUtil.getMealsTo(this.dao.findAll()));
            this.forward(request, response, LIST);
            return;
        }

        Meal meal = new Meal(id, dateTime, description, calories);
        this.dao.save(meal);
        if (id == null) {
            log.debug("save new meal={}", meal);
        } else {
            log.debug("save edited meal={}", meal);
        }

        request.setAttribute("mealToList", MealsUtil.getMealsTo(this.dao.findAll()));
        this.forward(request, response, LIST);
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals list");
        request.setAttribute("mealToList", MealsUtil.getMealsTo(this.dao.findAll()));
        this.forward(request, response, LIST);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to add meal");
        request.setAttribute("meal", new Meal());
        request.setAttribute("editaction", "insert");
        this.forward(request, response, INSERT_OR_EDIT);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Meal meal = dao.findById(this.getId(request));
        if (meal != null) {
            log.debug("redirect to edit meal={}", meal);
            request.setAttribute("meal", meal);
            request.setAttribute("editaction", "edit");
            this.forward(request, response, INSERT_OR_EDIT);
        } else {
            this.add(request, response);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Meal meal = dao.deleteById(this.getId(request));
        log.debug("redirect to delete meal = {}", meal);
        request.setAttribute("mealToList", MealsUtil.getMealsTo(this.dao.findAll()));
        response.sendRedirect("meals");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String forward) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    private Integer getId(HttpServletRequest request) {
        String idString = request.getParameter("id");
        return idString != null && !idString.isEmpty() ? Integer.parseInt(request.getParameter("id")) : null;
    }
}
