package com.stubedavd.match.entity;

import com.stubedavd.player.entity.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "matches") // "matches" является зарезервированным словом в некоторых СУБД. Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "Использование зарезервированных слов в качестве названий в БД.md" в этом же пакете)

@NoArgsConstructor // для Hibernate достаточно protected
@Getter
@Setter // TODO: сеттеры не нужны — позволяют создать объект с установленным id или бесконтрольно изменить состав игроков после создания (например сделать постороннего игрока победителем)
@ToString // Использование @ToString с @Entity может создавать проблемы (см. файл "Использование `@ToString` (Lombok) и `@Entity` (JPA) в одном классе.md" в этом же пакете)
public class Match {

    // Стоит добавить проверки, что игроки разные и победитель один из игроков. Например через аннотацию org.hibernate.annotations.Check над классом.

    // Поле `id` имеет тип `Integer`, который имеет максимальное значение `~2.1` миллиарда.
        // Хотя `Integer` соответствует ТЗ (`Int`), в долгосрочной перспективе это может стать проблемой.
        // Максимальное значение `Integer` может быть исчерпано в системах с большим количеством записей.
        // Общепринятой и хорошей практикой для первичных ключей является использование типа `Long`.
        // Лучше заменить тип поля `id` на `Long`.

    // TODO: Для корректного и безопасного создания новых, ещё не сохранённых в БД, матчей стоит создать и использовать конструктор со всеми полями, кроме ID.

    // Связи `@ManyToOne` не имеют явного указания о стратегии загрузки.
        // По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `MatchEntity`.
        // Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.

    // Для обязательных полей стоит добавить `optional = false` в `@ManyToOne` или `nullable = false` в `@JoinColumn` (можно добавить оба параметра).
        // Целостность данных должна обеспечиваться на всех уровнях: в приложении (валидация) и в БД (constraints). Отсутствие ограничений в БД означает,
        // что данные могут быть испорчены из-за ошибок в приложении или при прямом доступе к БД.
        //
        // А также можно добавить атрибут `updatable = false`. Это атрибут запрещает изменять колонку после её первоначальной вставки.
        // Игроки матча и победитель не должны меняться, поэтому эти колонки можно защитить от обновлений.

    // Колонки игроков и победителя в `@JoinColumn` названы `player1`, `player2`, `winner`.
        // Для колонок, хранящих внешний ключ, уместно добавлять суффикс `_id`, чтобы было очевидно, что в них хранится идентификатор, а не какая-то другая информация.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "player1") // Можно добавить nullable = false и updatable = false
    private Player player1;

    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "player2") // Можно добавить nullable = false и updatable = false
    private Player player2;

    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "winner") // Можно добавить nullable = false и updatable = false
    private Player winner;
}
