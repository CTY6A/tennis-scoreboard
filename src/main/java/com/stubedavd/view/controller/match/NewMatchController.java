package com.stubedavd.view.controller.match;

import com.stubedavd.exception.NotFoundException;
import com.stubedavd.exception.ValidationException;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.mapper.player.PlayerMapper;
import com.stubedavd.model.player.domain.PlayerDomain;
import com.stubedavd.model.player.entity.Player;
import com.stubedavd.model.player.repository.PlayerRepository;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше вынести в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет отправляет сообщение из исключения (`e.getMessage()`) напрямую пользователю для `ValidationException`.
        // Сообщения об ошибках из исключений могут содержать технические детали, которые не предназначены
        // для конечного пользователя и могут представлять угрозу безопасности. Например, сообщение может быть
        // `"No entity found for query 'SELECT ...'"` или `"Validation failed for field 'internalFieldName'"`,
        // что раскрывает структуру БД или внутренние имена полей.
        //
        // Хотя в этом приложении данная практика не приводит к проблемам
        // из-за того, что сообщения "безопасные", она все равно является плохим архитектурным решением.
        //
        // Лучше никогда не отправлять необработанное сообщение из исключения на клиент.
        // Вместо этого можно использовать заранее определённые, безопасные сообщения или коды ошибок.
        // Само исключение при этом нужно логировать для разработчиков.
        //
        // Это повысит безопасность приложения и улучшит пользовательский опыт при возникновении ошибок.

    public static final String JSP = "/WEB-INF/jsp/new-match.jsp";

    private OngoingMatchService ongoingMatchService;

    private PlayerMapper playerMapper;

    private PlayerRepository playerRepository;

    // Для получения объектов из контекста можно использовать "естественные константы" — Service.class.getSimpleName() или Service.class.getName()
    // Логику получения бина и проверку его на null можно вынести в базовый контроллер в специальный метод, чтобы она не повторялась по нескольку раз в каждом сервлете.
    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        ongoingMatchService =
                (OngoingMatchService) config
                        .getServletContext()
                        .getAttribute(OngoingMatchService.class.getSimpleName());

        if (ongoingMatchService == null) {

            throw new NotFoundException("Ongoing match service not found");
        }

        playerMapper =
                (PlayerMapper) config.getServletContext().getAttribute(PlayerMapper.class.getSimpleName());

        if (playerMapper == null) {

            throw new NotFoundException("Player mapper not found");
        }

        playerRepository =
                (PlayerRepository) config.getServletContext().getAttribute(PlayerRepository.class.getSimpleName());

        if (playerRepository == null) {
            throw new NotFoundException("Player repository not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String player1Name = request.getParameter("player1Name");
        String player2Name = request.getParameter("player2Name");

        try {

            // Validator лучше внедрять через метод init(), а не обращать к нему напрямую из этого метода
            // Можно создавать DTO с именами обоих игроков и передавать его на валидацию. А затем его же передавать в сервис.
            Validator.validatePlayers(player1Name, player2Name);

        // Логику обработки исключений можно реализовать в фильтре. Так она будет централизована для всего приложения и её части не будут повторяться в разных местах.
        } catch (ValidationException e) {

            // TODO: Не стоит отправлять сообщение из исключения (e.getMessage()) напрямую во View
            request.setAttribute("errorMessage", e.getMessage());

            request.setAttribute("player1Name", player1Name);
            request.setAttribute("player2Name", player2Name);

            request.getRequestDispatcher(JSP).forward(request, response);
        }

        player1Name = player1Name.trim();
        player2Name = player2Name.trim();

        String finalPlayer1Name = player1Name;
        String finalPlayer2Name = player2Name;

        Player player1 = playerRepository.findByName(player1Name)
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(finalPlayer1Name)));

        Player player2 = playerRepository.findByName(player2Name)
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(finalPlayer2Name)));

        PlayerDomain player1Domain = playerMapper.toDomain(player1);
        PlayerDomain player2Domain = playerMapper.toDomain(player2);

        UUID matchId = ongoingMatchService.save(player1Domain, player2Domain);

        response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + matchId);
    }
}
