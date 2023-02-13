package Gr8G1.prac.todo.mapper;

import Gr8G1.prac.todo.dto.TodoDto;
import Gr8G1.prac.todo.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TodoMapper {

  Todo todoDtoPostToTodo(TodoDto.POST requestBody);

  default List<TodoDto.Response> todoToTodoDtoResponseList(List<Todo> listTodo, String serviceUrl) {
    return listTodo.stream().map(todo -> {
      return new TodoDto.Response(
        todo.getId(),
        todo.getTitle(),
        todo.getOrder(),
        todo.getCompleted(),
        serviceUrl + "/" + todo.getId()
      );
    })
      .collect(Collectors.toList());
  };

  Todo todoDtoPatchToTodo(TodoDto.Patch reqeustBody);

  default TodoDto.Response todoToTodoDtoResponse(Todo todo, String serviceUrl) {
    return new TodoDto.Response(
      todo.getId(),
      todo.getTitle(),
      todo.getOrder(),
      todo.getCompleted(),
      serviceUrl + "/" + todo.getId()
    );
  };
}
