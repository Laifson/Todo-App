package de.neueforellen.backend.controller;

import de.neueforellen.backend.model.ToDo;
import de.neueforellen.backend.repo.ToDoRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ToDoControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ToDoRepo repo;

    @BeforeEach
    public void clearRepo() {
        repo.clearRepo();
    }

    @Test
    public void testGetMappingForAllToDos() {
        //GIVEN

        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        repo.addTodo(new ToDo("2", "Zweite Aufgabe", "IN_PROGRESS"));
        repo.addTodo(new ToDo("3", "Dritte Aufgabe", "DONE"));

        //WHEN
        ResponseEntity<ToDo[]> response = restTemplate.getForEntity("/api/todo", ToDo[].class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), arrayContainingInAnyOrder(
                new ToDo("1", "Erste Aufgabe", "OPEN"),
                new ToDo("2", "Zweite Aufgabe", "IN_PROGRESS"),
                new ToDo("3", "Dritte Aufgabe", "DONE"
                )));
    }

    @Test
    public void testGetMappingSpecificID() {
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        //WHEN
        ResponseEntity<ToDo> response = restTemplate.getForEntity("/api/todo/1", ToDo.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new ToDo("1", "Erste Aufgabe", "OPEN")));
    }

    @Test
    public void testdeleteToDoByID() {
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        repo.addTodo(new ToDo("2", "Zweite Aufgabe", "IN_PROGRESS"));
        repo.addTodo(new ToDo("3", "Dritte Aufgabe", "DONE"));
        //WHEN
        restTemplate.delete("/api/todo/1");
        //THEN
        assertThat(repo.findById("2"), is(Optional.of(new ToDo("2", "Zweite Aufgabe", "IN_PROGRESS"))));
        assertThat(repo.findById("3"), is(Optional.of(new ToDo("3", "Dritte Aufgabe", "DONE"))));
        assertThat(repo.findById("1"), is(Optional.empty()));
    }

    @Test
    public void testaddTodo() {
        //GIVEN

        //WHEN
        ResponseEntity<ToDo> response = restTemplate.postForEntity("/api/todo/", new ToDo("1", "Erste Aufgabe", "OPEN"), ToDo.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(repo.findById("1"), is(Optional.of(new ToDo("1", "Erste Aufgabe", "OPEN"))));
    }

    @Test
    public void testeditToDoStatus() {
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        //WHEN
        HttpEntity<ToDo> request = new HttpEntity<>(new ToDo("1", "Erste Aufgabe", "IN_PROGRESS"));
        ResponseEntity<ToDo> response = restTemplate.exchange("/api/todo/1",
                HttpMethod.PUT,
                request,
                ToDo.class
        );
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(repo.findById("1").get().getStatus(), is("IN_PROGRESS"));
    }

    @Test
    public void testgetToDoByIDException() {
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        //WHEN
        ResponseEntity<ToDo> response = restTemplate.getForEntity("/api/todo/2", ToDo.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}