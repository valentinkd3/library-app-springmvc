package ru.kozhevnikov.library.modelDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kozhevnikov.library.model.Book;
import ru.kozhevnikov.library.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }
    public Person show(int id) {
        List<Book> books =
                jdbcTemplate.query(
                        "SELECT * FROM person JOIN book ON person.person_id = book.person_id WHERE person.person_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class));
        Person person = jdbcTemplate.query("SELECT * FROM Person WHERE person_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
        person.setTakenBooks(books);

        return person;
    }
    public Optional<Person> show(String name) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE name=?", new Object[]{name},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
    public void save(Person person){
        jdbcTemplate.update(
          "INSERT INTO Person (name, year) VALUES (?,?)", person.getName(), person.getYear()
        );
    }
    public void update(int id, Person person){
        jdbcTemplate.update(
                "UPDATE Person SET name=?, year=? WHERE person_id=?",
                person.getName(), person.getYear(), id
        );
    }
    public void delete(int id){
        jdbcTemplate.update(
                "DELETE FROM Person WHERE person_id=?",
                id
        );
    }
}
