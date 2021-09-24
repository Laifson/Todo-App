package de.neueforellen.backend.service;

import de.neueforellen.backend.model.ToDo;
import de.neueforellen.backend.repo.ToDoRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ToDoServiceTest {

    private ToDoRepo repo = Mockito.mock(ToDoRepo.class);

    private ToDoService service = new ToDoService(repo);

    @BeforeEach
    public void clearRepo(){
        repo.clearRepo();
    }

    @Test
    public void voidgetAllTodosTest(){
        //GIVEN
        when(repo.getAllTodos()).thenReturn(List.of(
                new ToDo("1", "Erster", "OPEN"),
                new ToDo("2", "Zweiter", "OPEN")
        ));
        //WHEN

        List<ToDo> result = service.getAllTodos();

        //THEN
        assertThat(result, is(List.of(
                new ToDo("1", "Erster", "OPEN"),
                new ToDo("2", "Zweiter", "OPEN")
        )));
    }

    @Test
    public void getToDoByIdTest(){
        //GIVEN
        ToDo toFind = new ToDo("1", "Erster", "OPEN");
        when(repo.findById("1")).thenReturn(Optional.of(toFind));
        //WHEN

        ToDo result = service.getToDoById("1");

        //THEN
        assertThat(result, is(toFind));
    }

    @Test
    public void getToDoByIdTestNotFound(){
        //GIVEN

        //WHEN

        try {
            ToDo result = service.getToDoById("1");
            fail();
        }catch (IllegalArgumentException e){
            assertThat(e.getMessage(), is("Todo not found by id: 1"));
        }
    }

}