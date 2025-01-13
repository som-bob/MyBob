package com.my.bob.core.domain.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> implements Page<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;

    public PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @Override
    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    @Override
    public <U> Page<U> map(java.util.function.Function<? super T, ? extends U> converter) {
        List<U> mappedContent = (List<U>) content.stream().map(converter).toList();
        return new PageResponse<>(mappedContent, pageNumber, pageSize, totalElements);
    }

    @Override
    public int getNumber() {
        return pageNumber;
    }

    @Override
    public int getSize() {
        return pageSize;
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        // Default implementation for unsorted data.
        return Sort.unsorted();
    }

    @Override
    public boolean isFirst() {
        return pageNumber == 0;
    }

    @Override
    public boolean isLast() {
        return pageNumber == getTotalPages() - 1;
    }

    @Override
    public boolean hasNext() {
        return pageNumber < getTotalPages() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? Pageable.ofSize(pageSize).withPage(pageNumber + 1) : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? Pageable.ofSize(pageSize).withPage(pageNumber - 1) : Pageable.unpaged();
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
