package io.github.notoday.bff.service.dto;

import io.github.notoday.commons.core.domain.IPage;

/**
 * @author no-today
 * @date 2021/10/11 5:39 PM
 */
public class PageDTO {

    private int page;
    private int size;

    private int totalPages;
    private long totalCounts;

    private boolean hasNext;
    private boolean hasPrevious;

    public static PageDTO of(IPage<?> iPage) {
        return new PageDTO()
            .setPage(iPage.getPage())
            .setSize(iPage.getSize())
            .setTotalPages(iPage.getTotalPages())
            .setTotalCounts(iPage.getTotalCounts())
            .setHasNext(iPage.isHasNext())
            .setHasPrevious(iPage.isHasPrevious());
    }

    public int getPage() {
        return page;
    }

    public PageDTO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public PageDTO setSize(int size) {
        this.size = size;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public PageDTO setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public long getTotalCounts() {
        return totalCounts;
    }

    public PageDTO setTotalCounts(long totalCounts) {
        this.totalCounts = totalCounts;
        return this;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public PageDTO setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public PageDTO setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
        return this;
    }
}
