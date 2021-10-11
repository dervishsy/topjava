package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepositoryImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static String INSERT_OR_EDIT = "/mealEdit.jsp";
    private static String LIST = "/meals.jsp";

    private static final Logger log = getLogger(MealServlet.class);
    private MealRepositoryImpl dao;

    public MealServlet() {
        this.dao = new MealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forward="";
        String action = request.getParameter("action");
        log.debug("redirect to meals "+action);
        if (action==null) action = "list";

        if (action.equalsIgnoreCase("delete")){
            int userId = Integer.parseInt(request.getParameter("Id"));
            dao.deleteById(userId);
            forward = LIST;
            request.setAttribute("mealToList", dao.getMealsTo());
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("Id"));
            Meal meal = dao.findById(id);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("list")){
            forward = LIST;
            request.setAttribute("mealToList", dao.getMealsTo());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("Id");

        String dateTimeString = request.getParameter("dateTime");
        String description = request.getParameter("description");
        String caloriesString = request.getParameter("calories");

        //--------------- date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        //--------------- date
        int calories = Integer.parseInt(caloriesString);
        int id ;
        if(idString == null || idString.isEmpty()) {
            id = Integer.MIN_VALUE;
        }else {
            id =  Integer.parseInt(idString);
        }

        Meal meal = new Meal(id,dateTime,description, calories);
        dao.save(meal);

        RequestDispatcher view = request.getRequestDispatcher(LIST);
        request.setAttribute("mealToList", dao.getMealsTo());
        view.forward(request, response);
    }
}
