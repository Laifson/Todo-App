package de.neueforellen.backend.repo;

import de.neueforellen.backend.model.ToDo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ToDoRepoTest {
    ToDoRepo repo = new ToDoRepo();

    @BeforeEach
    public void clearRepo(){
        repo.clearRepo();
    }
    @Test
    public void getAllTodosTest(){
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        repo.addTodo(new ToDo("2", "Zweite Aufgabe", "OPEN"));
        repo.addTodo(new ToDo("3", "Dritte Aufgabe", "IN_PROGRESS"));
        //WHEN
        List<ToDo> result = repo.getAllTodos();
        //THEN
        assertThat(result, is(List.of(
                new ToDo("1", "Erste Aufgabe", "OPEN"),
                new ToDo("2", "Zweite Aufgabe", "OPEN"),
                new ToDo("3", "Dritte Aufgabe", "IN_PROGRESS")
        )));
    }

    @Test
    public void findByIdTest(){
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        //WHEN
        Optional<ToDo> result = repo.findById("1");
        //THEN
        assertThat(result, is(Optional.of(new ToDo("1", "Erste Aufgabe", "OPEN"))));
    }

    @Test
    public void findByIdTestNotFound(){
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        //WHEN
        Optional<ToDo> result = repo.findById("2");
        //THEN
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void deleteToDoTest(){
        //GIVEN
        repo.addTodo(new ToDo("1", "Erste Aufgabe", "OPEN"));
        repo.addTodo(new ToDo("2", "Zweite Aufgabe", "IN_PROGRESS"));
        repo.addTodo(new ToDo("3", "Dritte Aufgabe", "OPEN"));
        //WHEN
        repo.deleteToDo("2");
        List<ToDo> result = repo.getAllTodos();
        //THEN
        assertThat(result, containsInAnyOrder(
                new ToDo("1", "Erste Aufgabe", "OPEN"),
                new ToDo("3", "Dritte Aufgabe", "OPEN")
        ));
    }

    @Test
    public void addToDoTestWithNoID(){
        //GIVEN
        repo.addTodo(new ToDo("Erste Aufgabe", "OPEN"));
        //WHEN
        List<ToDo> result = repo.getAllTodos();
        //THEN
        assertThat(result.get(0).getDescription(), is("Erste Aufgabe"));
    }

    @Test
    public void addToDoTestWithExistingID(){
        //GIVEN
        repo.addTodo(new ToDo("1","Erste Aufgabe", "OPEN"));
        //WHEN
        try {
            repo.addTodo(new ToDo("1","Erste Aufgabe", "OPEN"));
            fail();
        }catch (IllegalArgumentException e){
            assertThat(e.getMessage(), is("Id already exists"));
        }
    }

    @Test
    public void editToDoTest(){
        //GIVEN
        repo.addTodo(new ToDo("1","Erste Aufgabe", "OPEN"));
        //WHEN
        repo.editToDo(new ToDo("1", "Erste Aufgabe", "DONE"));
        //THEN
        assertThat(repo.findById("1"), is(Optional.of(new ToDo("1", "Erste Aufgabe", "DONE"))));
    }

    @Test
    public void editToDoTestNotExistingToDo(){
        //GIVEN
        repo.addTodo(new ToDo("1","Erste Aufgabe", "OPEN"));
        //WHEN
        try {
            repo.editToDo(new ToDo("3", "Erste Aufgabe", "DONE"));
            fail();
        }catch (IllegalArgumentException e){
            assertThat(e.getMessage(), is("Cant find ToDo with ID"));
        }
    }
}