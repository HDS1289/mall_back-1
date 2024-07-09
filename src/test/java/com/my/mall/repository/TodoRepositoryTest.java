package com.my.mall.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.my.mall.domain.entity.Todo;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class TodoRepositoryTest {
	@Autowired private TodoRepository todoRepository;
	
	@Test
	public void insertTodo() {
		for(int i = 1; i <= 100; i++) {
			Todo todo = Todo.builder()
					.title("title ")
					.dueDate(LocalDate.now().plusDays(i))
					.writer("writer " + i)
					.build();
			todoRepository.save(todo);
		}
	}
	
	@Test
	public void selectTodo() {
		Long todoNo = 2L;
		Optional<Todo> result = todoRepository.findById(todoNo);
		Todo todo = result.orElseThrow();
		log.info(todo);
 	}
	
	@Test
	public void updateTodo() {
		Long todoNo = 2L;
		Optional<Todo> result = todoRepository.findById(todoNo);
		Todo todo = result.orElseThrow();
		
		todo.setTitle("제목");
		todo.setDone(true);
		todo.setDueDate(LocalDate.now());
		
		todoRepository.save(todo);
	}
	
	@Test
	public void deleteTodo() {
		Long todoNo = 2L;
		todoRepository.deleteById(todoNo);
	}
	
	@Test
	public void testPaging() {
		Pageable pageable = 
				PageRequest.of(0, 10, Sort.by("todoNo").descending());
		Page<Todo> result = todoRepository.findAll(pageable);
		log.info(result.getTotalElements());
		result.getContent().forEach(todo -> log.info(todo));
	}
}
