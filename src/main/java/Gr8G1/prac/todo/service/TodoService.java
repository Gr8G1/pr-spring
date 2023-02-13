package Gr8G1.prac.todo.service;

import Gr8G1.prac.todo.entity.Todo;
import Gr8G1.prac.todo.repository.TodoRepository;
import Gr8G1.prac.todo.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {
  private final TodoRepository todoRepository;
  private final CustomBeanUtils<Todo> customBeanUtils;

  public Todo createTodo(Todo todo) {
    return todoRepository.save(todo);
  }

  public List<Todo> findAllTodo() {
    return todoRepository.findAll();
  }

  public Todo findTodo(Long id) {
    return findTodoById(id);
  }

  public Todo updateTodo(Long id, Todo todo) {
    Todo todoById = findTodoById(id);

    Todo updatedTodo = customBeanUtils.copyNonNullProperties(todo, todoById);

    return todoRepository.save(updatedTodo);
  }

  public void deleteTodo(Long id) {
    todoRepository.deleteById(id);
  }

  public void deleteAllTodo() {
    todoRepository.deleteAll();
  }

  private Todo findTodoById(Long id) {
    Optional<Todo> byId = todoRepository.findById(id);

    return byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
