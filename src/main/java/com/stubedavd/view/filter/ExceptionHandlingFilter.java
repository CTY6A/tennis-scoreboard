package com.stubedavd.view.filter;

import com.stubedavd.exception.EntityAlreadyExistException;
import com.stubedavd.exception.DatabaseException;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.exception.ValidationException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {
    private static final String UNKNOWN_SERVER_ERROR = "Unknown server error";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException {
        try {
            chain.doFilter(req, res);
        } catch (DatabaseException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (EntityAlreadyExistException e) {
            res.sendError(HttpServletResponse.SC_CONFLICT);
        } catch (ValidationException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, UNKNOWN_SERVER_ERROR);
        }
    }
}
