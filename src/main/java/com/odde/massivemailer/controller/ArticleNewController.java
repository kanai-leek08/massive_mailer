package com.odde.massivemailer.controller;

import com.odde.massivemailer.model.ContactPerson;
import com.odde.massivemailer.model.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/article_new")
public class ArticleNewController extends AppController {


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("article_new.jsp");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("articles.jsp");
    }

}
