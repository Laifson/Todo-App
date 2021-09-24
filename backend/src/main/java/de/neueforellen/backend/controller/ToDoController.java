package de.neueforellen.backend.controller;

import de.neueforellen.backend.model.ToDo;
import de.neueforellen.backend.service.ToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ToDoController {

    ToDoService service;

    public ToDoController(ToDoService service) {
        this.service = service;
    }

    @GetMapping("api/todo")
    public List<ToDo> getAllTodos(){
        return service.getAllTodos();
    }

    @GetMapping("api/todo/{id}")
    public ToDo getToDoByID(@PathVariable String id){
        try{
            return service.getToDoById(id);
        }catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("api/todo/{id}")
    public void deleteToDoByID(@PathVariable String id){
        service.deleteToDoByID(id);
    }

    @PostMapping("api/todo")
    public ToDo addTodo(@RequestBody ToDo toAdd){
        return service.addTodo(toAdd);
    }


    @PutMapping("api/todo/{id}")
    public ToDo editToDoStatus(@PathVariable String id, @RequestBody ToDo toDo){
        return service.editToDoStatus(toDo);
    }

}
