package com.stubedavd.filter;

import com.stubedavd.exception.EntityAlreadyExistException;
import com.stubedavd.exception.DatabaseException;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.exception.ValidationException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    // Класс может наследоваться от HttpFilter и переопределять его метод
        // protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain),
        // тогда не придётся ServletResponse к HttpServletResponse вручную.

    // TODO: Фильтр отправляет сообщение из исключения (`e.getMessage()`) напрямую пользователю.
        // Сообщения об ошибках из исключений могут содержать технические детали, которые не предназначены
        // для конечного пользователя и могут представлять угрозу безопасности. Например, сообщение может быть
        // `"No entity found for query 'SELECT ...'"` или `"Validation failed for field 'internalFieldName'"`,
        // что раскрывает структуру БД или внутренние имена полей.
        //
        // Лучше никогда не отправлять необработанное сообщение из исключения на клиент.
        // Вместо этого можно использовать заранее определённые, безопасные сообщения или коды ошибок.
        // Само исключение при этом нужно логировать для разработчиков.
        //
        // Это повысит безопасность приложения и улучшит пользовательский опыт при возникновении ошибок.

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

        } catch (EntityAlreadyExistException e) {

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
