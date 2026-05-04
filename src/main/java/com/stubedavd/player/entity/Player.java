package com.stubedavd.player.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "players") // можно задать индекс через аннотацию, чтобы у него было понятное имя — @Table(name = "players", indexes = @Index(...))

@NoArgsConstructor // для Hibernate достаточно protected
@Getter
@Setter // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить имя игрока после создания
@ToString
public class Player {

    // Поле `id` имеет тип `Integer`, который имеет максимальное значение `~2.1` миллиарда.
        // Хотя `Integer` соответствует ТЗ (`Int`), в долгосрочной перспективе это может стать проблемой.
        // Максимальное значение `Integer` может быть исчерпано в системах с большим количеством записей.
        // Общепринятой и хорошей практикой для первичных ключей является использование типа `Long`.
        // Лучше заменить тип поля `id` на `Long`.

    // TODO: Для корректного и безопасного создания новых, ещё не сохранённых в БД, игроков стоит создать и использовать конструктор со всеми полями, кроме ID.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true) // можно добавить length = 50 (например), чтобы задать ограничения на уровне БД, а также nullable = false
    private String name;
}
