package Gr8G1.prac.todo.repository;

import Gr8G1.prac.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
