package de.neueforellen.backend.repo;

import de.neueforellen.backend.model.ToDo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ToDoRepo {

        private final Map<String, ToDo> toDoRepoMap = new HashMap();

        public List<ToDo> getAllTodos() {
            return List.copyOf(toDoRepoMap.values());
        }

        public Optional<ToDo> findById(String id) {
            return Optional.ofNullable(toDoRepoMap.get(id));
        }

        public Optional<ToDo> deleteToDo(String id) {
            return Optional.ofNullable(toDoRepoMap.remove(id));
        }

        public ToDo addTodo(ToDo toDo) throws IllegalArgumentException {
            toDo.setId(UUID.randomUUID().toString());
            if(toDoRepoMap.containsKey(toDo.getId())) {
                throw new IllegalArgumentException("Id already exists");
            }
            toDoRepoMap.put(toDo.getId(), toDo);
            return toDo;
        }

        public ToDo editToDo(String id, String status) {
            toDoRepoMap.get(id).setStatus(status);
            return toDoRepoMap.get(id);
        }


}
