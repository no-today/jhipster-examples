package io.github.notoday.commons.core.domain;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author no-today
 * @date 2021/10/13 4:07 PM
 */
public class IPage<T> {

    private List<T> list;

    private int page;
    private int size;

    private int totalPages;
    private long totalCounts;

    private boolean hasNext;
    private boolean hasPrevious;

    public static <T> IPage<T> of(Page<T> page) {
        return new IPage<T>()
                .setList(page.getContent())
                .setPage(page.getNumber())
                .setSize(page.getSize())
                .setTotalPages(page.getTotalPages())
                .setTotalCounts(page.getTotalElements())
                .setHasNext(page.hasNext())
                .setHasPrevious(page.hasPrevious());
    }

    public List<T> getList() {
        return list;
    }

    public IPage<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public int getPage() {
        return page;
    }

    public IPage<T> setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public IPage<T> setSize(int size) {
        this.size = size;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public IPage<T> setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public long getTotalCounts() {
        return totalCounts;
    }

    public IPage<T> setTotalCounts(long totalCounts) {
        this.totalCounts = totalCounts;
        return this;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public IPage<T> setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public IPage<T> setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
        return this;
    }
}