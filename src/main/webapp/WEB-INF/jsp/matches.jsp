<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">

    <script src="js/app.js"></script>
</head>

<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Matches</h1>
        <div class="input-container">
            <input class="input-filter" placeholder="Filter by name" type="text" name="playerNameFilter" id="playerNameFilter" value="${playerName}"/>
            <div>
                <a onclick="resetFilterAndSendGet(1)">
                    <button class="btn-filter">Reset Filter</button>
                </a>
            </div>
            <div>
                <a onclick="sendGet(1)">
                    <button class="btn-filter">Apply Filter</button>
                </a>
            </div>
        </div>

        <form id="getForm" action="${pageContext.request.contextPath}/matches" method="GET" style="display:none;">
            <input type="hidden" name="page" id="page">
            <input type="hidden" name="filter_by_player_name" id="filter_by_player_name">
        </form>

        <script>
            function sendGet(page) {
                document.getElementById('page').value = page;
                document.getElementById('filter_by_player_name').value = document.getElementById('playerNameFilter').value;
                document.getElementById('getForm').submit();
            }
            function resetFilterAndSendGet(page) {
                document.getElementById('page').value = page;
                document.getElementById('filter_by_player_name').value = "";
                document.getElementById('getForm').submit();
            }
        </script>

        <table class="table-matches">
            <tr>
                <th>Player One</th>
                <th>Player Two</th>
                <th>Winner</th>
            </tr>
            <c:forEach var="match" items="${requestScope.matches}">
                <tr>
                    <td>${match.player1Name()}</td>
                    <td>${match.player2Name()}</td>
                    <td><span class="winner-name-td">${match.winnerName()}</span></td>
                </tr>
            </c:forEach>
        </table>

        <div class="pagination">
            <a class="prev" onclick="sendGet(${pageNumber} - 1)"> < </a>
            <c:forEach begin="${(pageNumber > 2) ? pageNumber - 2 : 1}" end="${pageNumber + 2}" varStatus="loop">
                <c:if test="${(loop.index > 0) && (loop.index <= pageCount)}">
                    <a class="num-page
                    <c:if test="${loop.index == pageNumber}"> current</c:if>"
                        onclick="sendGet(${loop.index})">${loop.index}</a>
                </c:if>
            </c:forEach>
            <a class="next" onclick="sendGet(${pageNumber} + 1)"> > </a>
            <p> ${matchesFrom}-${matchesTo} of ${matchesCount}</p>
        </div>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>
