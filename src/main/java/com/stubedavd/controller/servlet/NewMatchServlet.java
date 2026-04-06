package com.stubedavd.controller.servlet;

import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.service.OngoingMatchService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/new-match")
public class NewMatchServlet extends BaseServlet {

    public static final String JSP = "/WEB-INF/jsp/new-match.jsp";

    private OngoingMatchService ongoingMatchService;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        ongoingMatchService =
                (OngoingMatchService) config.getServletContext().getAttribute(ContextListener.ONGOING_MATCH_SERVICE);

        if (ongoingMatchService == null) {
            throw new NotFoundException("Ongoing match service not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(JSP).forward(request, response);
    }
}
