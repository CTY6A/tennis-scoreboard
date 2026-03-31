package com.stubedavd.controller;

import com.stubedavd.model.entity.Match;
import com.stubedavd.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

@WebServlet("/new-match")
public class NewMatchServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Match> matches;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            matches = session.createQuery("from Match").list();
        }
        sendJson(resp, HttpServletResponse.SC_OK, matches);
    }
}
