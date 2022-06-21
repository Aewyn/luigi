package com.example.luigi.repositories;

import com.example.luigi.domain.Pizza;
import com.example.luigi.exceptions.PizzaNietGevondenException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PizzaRepository {
    private final SimpleJdbcInsert insert;
    private final JdbcTemplate template;
    public PizzaRepository(JdbcTemplate template){
        this.template = template;
        insert = new SimpleJdbcInsert(template)
                .withTableName("pizzas")
                .usingGeneratedKeyColumns("id");
    }

    public long create(Pizza pizza){
        return insert.executeAndReturnKey(Map.of("naam", pizza.getNaam(),"prijs", pizza.getPrijs(), "pikant", pizza.isPikant())).longValue();
    }

    public long findAantal(){
        var sql = """
                select count(*)
                from pizzas
                """;
        return template.queryForObject(sql, Long.class);
    }

    public List<Pizza> findAll(){
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                order by id
                """;
        return template.query(sql, pizzaMapper);
    }

    public void delete(long id){
        var sql = """
                delete from pizzas
                where id = ?
                """;
        template.update(sql, id);
    }

    public void update(Pizza pizza){
        var sql = """
                update pizzas
                set naam = ?, prijs = ?, pikant = ?
                where id = ?
                """;
        if(template.update(sql,pizza.getNaam(),pizza.getPrijs(),pizza.isPikant(),pizza.getId()) == 0){
            throw new PizzaNietGevondenException();
        }
    }

    private final RowMapper<Pizza> pizzaMapper = (result, rowNum)
            -> new Pizza(result.getLong("id"), result.getString("naam"), result.getBigDecimal("prijs"), result.getBoolean("pikant"));
}
