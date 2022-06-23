package com.example.luigi.repositories;

import com.example.luigi.domain.Pizza;
import com.example.luigi.exceptions.PizzaNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class PizzaRepository {
    private final SimpleJdbcInsert insert;
    private final JdbcTemplate template;

    public PizzaRepository(JdbcTemplate template) {
        this.template = template;
        insert = new SimpleJdbcInsert(template)
                .withTableName("pizzas")
                .usingGeneratedKeyColumns("id");
    }

    public long create(Pizza pizza) {
        return insert.executeAndReturnKey(Map.of("naam", pizza.getNaam(), "prijs", pizza.getPrijs(), "pikant", pizza.isPikant())).longValue();
    }

    public long findAantal() {
        var sql = """
                select count(*)
                from pizzas
                """;
        return template.queryForObject(sql, Long.class);
    }

    public List<Pizza> findAll() {
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                order by id
                """;
        return template.query(sql, pizzaMapper);
    }

    public List<Pizza> findBetween(BigDecimal van, BigDecimal tot) {
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                where prijs between ? and ?
                order by prijs
                """;
        return template.query(sql, pizzaMapper, van, tot);
    }

    public Optional<Pizza> findById(long id) {
        try {
            var sql = """
                    select id, naam, prijs, pikant
                    from pizzas
                    where id = ?
                    """;
            return Optional.of(template.queryForObject(sql, pizzaMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Pizza> findByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                where id in (
                """
                + "?,".repeat(ids.size() - 1)
                + "?) order by id";
        return template.query(sql, pizzaMapper, ids.toArray());
    }

    public List<Pizza> findByPrijs(BigDecimal prijs) {
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                where prijs = ?
                order by naam
                """;
        return template.query(sql, pizzaMapper, prijs);
    }

    private final RowMapper<BigDecimal> prijsMapper = (result, rowNum) -> result.getBigDecimal("prijs");

    public List<BigDecimal> findUniekePrijzen() {
        var sql = """
                select distinct prijs
                from pizzas
                order by prijs
                """;
        return template.query(sql, prijsMapper);
    }

    public void delete(long id) {
        var sql = """
                delete from pizzas
                where id = ?
                """;
        template.update(sql, id);
    }

    public void update(Pizza pizza) {
        var sql = """
                update pizzas
                set naam = ?, prijs = ?, pikant = ?
                where id = ?
                """;
        if (template.update(sql, pizza.getNaam(), pizza.getPrijs(), pizza.isPikant(), pizza.getId()) == 0) {
            throw new PizzaNietGevondenException();
        }
    }

    private final RowMapper<Pizza> pizzaMapper = (result, rowNum)
            -> new Pizza(result.getLong("id"), result.getString("naam"), result.getBigDecimal("prijs"), result.getBoolean("pikant"));
}
