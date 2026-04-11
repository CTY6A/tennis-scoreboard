package com.stubedavd.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stubedavd.exception.AlreadyExistException;
import com.stubedavd.exception.DatabaseException;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.exception.ValidationException;
import com.stubedavd.listener.ContextListener;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Filter.super.init(filterConfig);

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {

            chain.doFilter(request, response);

        } catch (DatabaseException e) {

            writeError(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

        } catch (AlreadyExistException e) {

            writeError(httpResponse, HttpServletResponse.SC_CONFLICT, e.getMessage());

        } catch (ValidationException e) {

            writeError(httpResponse, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

        } catch (NotFoundException e) {

            writeError(httpResponse, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {

            writeError(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unknown server error");
        }
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {

        response.sendError(status, message);
    }
}
