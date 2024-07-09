package com.my.mall.domain.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;

@Data
public class PageResponseDto<E> {
	private List<E> items;
	private List<Integer> pageNums;
	private PageRequestDto request;
	private boolean prev, next;
	private int currentPage, prevPage, nextPage, totPageCnt, totItemCnt;
	
	@Builder
	public PageResponseDto(List<E> items, PageRequestDto request, long totItemCnt) {
		this.items = items;
		this.request = request;
		this.totItemCnt = (int)totItemCnt;
		int end = (int)(Math.ceil(request.getPage() / 10.0)) * 10;
		int start = end - 9;
		int last = (int)(Math.ceil((totItemCnt/(double)request.getSize())));
		end = end > last ? last : end; // 글 갯수가 10개가 안 될 수 도 있다. end page의 조정
		this.prev = start > 1;
		this.next = totItemCnt > end * request.getSize();
		this.pageNums = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
		if(prev) this.prevPage = start - 1;
		if(next) this.nextPage = end + 1;
		this.totPageCnt = this.pageNums.size();
		this.currentPage = request.getPage();
	}
}
