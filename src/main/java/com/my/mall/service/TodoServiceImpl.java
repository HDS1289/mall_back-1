package com.my.mall.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.mall.domain.dto.PageRequestDto;
import com.my.mall.domain.dto.PageResponseDto;
import com.my.mall.domain.dto.TodoDto;
import com.my.mall.domain.entity.Todo;
import com.my.mall.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor // DI작업 ==> setter or constructor
public class TodoServiceImpl implements TodoService {
	private final ModelMapper modelMapper; // final로 DI작업 시행
	private final TodoRepository todoRepository;
	
	@Override
	public TodoDto getTodo(Long todoNo) {
		Optional<Todo> result = todoRepository.findById(todoNo);
		Todo todo = result.orElseThrow();
		TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
		return todoDto;
	}
	
	@Override
	public void addTodo(TodoDto todoDto) {
		Todo todo = modelMapper.map(todoDto, Todo.class);
		todoRepository.save(todo);
	}
	
	@Override
	public void fixTodo(TodoDto todoDto) {
		Optional<Todo> result = todoRepository.findById(todoDto.getTodoNo());
		Todo todo = result.orElseThrow();
		
		todo.setTitle(todoDto.getTitle());
		todo.setDueDate(todoDto.getDueDate());
		todo.setDone(todoDto.isDone());
		
		todoRepository.save(todo);
	}
	
	@Override
	public void delTodo(Long todoNo) {
		todoRepository.deleteById(todoNo);
	}
	
	@Override
	public PageResponseDto<TodoDto> getTodos(PageRequestDto request) {
		Pageable pageable = PageRequest.of(
					request.getPage() - 1,
					request.getSize(),
					Sort.by("todoNo").descending());
		Page<Todo> page = todoRepository.findAll(pageable);
		List<TodoDto> todoDtos = page.getContent().stream()
				.map(todo -> modelMapper.map(todo, TodoDto.class))
				.collect(Collectors.toList());
		
		PageResponseDto<TodoDto> response = PageResponseDto.<TodoDto>builder()
				.items(todoDtos)
				.request(request)
				.totItemCnt(page.getTotalElements())
				.build();
		
		return response;
	}
}
