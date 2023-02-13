package Gr8G1.prac.todo.controller;

import Gr8G1.prac.todo.entity.Todo;
import Gr8G1.prac.todo.mapper.TodoMapper;
import Gr8G1.prac.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import Gr8G1.prac.todo.dto.TodoDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
// @CrossOrigin(origins = "*", allowedHeaders = "*")
public class TodoController {
  private final TodoService todoService;
  private final TodoMapper todoMapper;

  @Value("http://localhost:8080")
  private String serviceUrl;

  @PostMapping

  public ResponseEntity<?> postToto(@RequestBody TodoDto.POST requestBody) {
    Todo todo = todoMapper.todoDtoPostToTodo(requestBody);

    if (ObjectUtils.isEmpty(todo.getTitle())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    if (ObjectUtils.isEmpty(todo.getCompleted())) todo.setCompleted(false);
    if (ObjectUtils.isEmpty(todo.getOrder())) todo.setOrder(0);

    Todo createTodo = todoService.createTodo(todo);

    TodoDto.Response response = todoMapper.todoToTodoDtoResponse(createTodo, serviceUrl);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findTodo(@PathVariable("id") Long id) {
    Todo todo = todoService.findTodo(id);

    return new ResponseEntity<>(todoMapper.todoToTodoDtoResponse(todo, serviceUrl), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<?> findAllTodo(HttpServletRequest request) {

    List<Todo> listTodo = todoService.findAllTodo();
    List<TodoDto.Response> responses = todoMapper.todoToTodoDtoResponseList(listTodo, serviceUrl);

    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateTodo(@PathVariable("id") Long id, @RequestBody TodoDto.Patch requestBody) {
    Todo updatedTodo = todoService.updateTodo(id, todoMapper.todoDtoPatchToTodo(requestBody));

    return new ResponseEntity<>(todoMapper.todoToTodoDtoResponse(updatedTodo, serviceUrl), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTodo(@PathVariable("id") Long id) {
    todoService.deleteTodo(id);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAllTodo() {
    todoService.deleteAllTodo();

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
