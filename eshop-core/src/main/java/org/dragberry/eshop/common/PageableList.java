package org.dragberry.eshop.common;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableList<T> {

	private List<T> content;
	
	private long pageNumber;
	
	private long pageSize;

	private long totalPages;
	
	private long total;
	
	private PageableList(List<T> content, long pageNumber, long pageSize, long totalPages, long total) {
		this.content = content;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.total = total;
		this.totalPages = totalPages;
	}
	
	public static <T> PageableList<T> of(List<T> content, long pageNumber, long pageSize, long totalPages, long total) {
		return new PageableList<>(content, pageNumber, pageSize, totalPages, total);
	}
	
	public static <T> PageableList<T> of(Page<T> page) {
		return new PageableList<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
	}
}
