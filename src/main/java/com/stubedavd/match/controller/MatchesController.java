package com.stubedavd.match.controller;

import com.stubedavd.match.model.dto.response.MatchResponseDto;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.match.model.service.MatchesService;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше вынести в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет берёт на себя лишнюю ответственность — содержит бизнес-логику,
        // хотя его задача — только принимать HTTP-запросы и делегировать их обработку. Это нарушает принцип единственной ответственности (SRP)
        // и делает код сервлета более сложным и трудным для тестирования.
        // Сервлет должен быть "тонким контроллером", делегирующим всю бизнес-логику сервису.
        // (см. файл "Архитектурный анти-паттерн: "Толстый контроллер" (Fat Controller).md" в этом же пакете)

    public static final String JSP = "/WEB-INF/jsp/matches.jsp";

    public static final int PAGE_SIZE = 10;
    public static final int PAGE_NUMBER_0 = 0;

    private MatchesService matchesService;

    // Для получения объектов из контекста можно использовать "естественные константы" — Service.class.getSimpleName() или Service.class.getName()
    // Логику получения бина и проверку его на null можно вынести в базовый контроллер в специальный метод, чтобы она не повторялась по нескольку раз в каждом сервлете.
    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        matchesService =
                (MatchesService) config.getServletContext().getAttribute(ContextListener.MATCHES_SERVICE);

        if (matchesService == null) {
            throw new NotFoundException("Matches service not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<MatchResponseDto> matches;
        long pageNumber = getPageNumber(request);
        long pageCount;
        long matchesCount;

        // Использование Optional для проверки на null не является хорошим подходом.
        Optional<String> playerNameOptional = getPlayerFilter(request);

        // Сервлет не должен заниматься бизнес-логикой. Это обязанность сервисного слоя.
        matchesCount = playerNameOptional
                .map(playerNameFilter -> matchesService.getTotalCount(playerNameFilter))
                .orElseGet(() -> matchesService.getTotalCount());

        pageCount = (matchesCount + PAGE_SIZE - 1) / PAGE_SIZE;

        if (pageNumber > pageCount - 1 && pageCount != 0) {

            pageNumber = pageCount - 1;
        }

        int finalPageNumber = (int) pageNumber;

        // Сервлет не должен заниматься бизнес-логикой. Это обязанность сервисного слоя.
        matches = playerNameOptional
                .map(playerNameFilter -> matchesService.getPage(playerNameFilter, finalPageNumber, PAGE_SIZE))
                .orElseGet(() -> matchesService.getPage(finalPageNumber, PAGE_SIZE));

        // Вместо передачи данных во View по частям, лучше создать специальный DTO
        request.setAttribute("matches", matches);
        request.setAttribute("playerName", playerNameOptional.orElse(""));
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNumber", pageNumber + 1 );
        request.setAttribute("matchesCount", matchesCount);
        request.setAttribute("matchesFrom", pageNumber * PAGE_SIZE);
        request.setAttribute("matchesTo", pageNumber * PAGE_SIZE + matches.size());

        request.getRequestDispatcher(JSP).forward(request, response);
    }

    private int getPageNumber(HttpServletRequest request) {

        int pageNumber = PAGE_NUMBER_0;
        String pageNumberString = request.getParameter("page");

        if (pageNumberString != null && !pageNumberString.isBlank()) {

            pageNumberString = pageNumberString.trim();

            // Validator лучше внедрять через метод init(), а не обращать к нему напрямую из этого метода
            Validator.validatePageNumber(pageNumberString);
            pageNumber = Integer.parseInt(pageNumberString);

            pageNumber--;

            if (pageNumber < PAGE_NUMBER_0) {
                pageNumber = PAGE_NUMBER_0;
            }
        }

        return pageNumber;
    }

    private Optional<String> getPlayerFilter(HttpServletRequest request) {

        String playerName = request.getParameter("filter_by_player_name");

        if (playerName != null && !playerName.isBlank()) {

            playerName = playerName.trim();

            // Validator лучше внедрять через метод init(), а не обращать к нему напрямую из этого метода
            Validator.validatePlayerName(playerName);

            return Optional.of(playerName);
        }

        return Optional.empty();
    }
}
