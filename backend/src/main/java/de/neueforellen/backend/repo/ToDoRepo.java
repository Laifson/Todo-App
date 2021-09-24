package de.neueforellen.backend.repo;

import de.neueforellen.backend.model.ToDo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ToDoRepo {

    public ToDoRepo() {
    }

    private final Map<String, ToDo> toDoRepoMap = new HashMap();

        public List<ToDo> getAllTodos() {
            return List.copyOf(toDoRepoMap.values());
        }

        public Optional<ToDo> findById(String id) {
            return Optional.ofNullable(toDoRepoMap.get(id));
        }

        public boolean deleteToDo(String toDelete) {
            toDoRepoMap.remove(toDelete);
            return !toDoRepoMap.containsKey(toDelete);
        }

        public ToDo addTodo(ToDo toDo) throws IllegalArgumentException {
            toDo.setId(UUID.randomUUID().toString());
            if(toDoRepoMap.containsKey(toDo.getId())) {
                throw new IllegalArgumentException("Id already exists");
            }
            toDoRepoMap.put(toDo.getId(), toDo);
            return toDo;
        }

        public ToDo editToDo(ToDo toEdit) {
            if(toDoRepoMap.containsKey(toEdit.getId())){
                return toDoRepoMap.put(toEdit.getId(),toEdit);
            }
            throw new IllegalArgumentException("ToDo");
        }


}
