package br.com.fadesp.test.pagamento.domain.dto;

import java.util.List;

public class PageDTO<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int pageSize;

    public PageDTO(
            List<T> content,
            long totalElements,
            int currentPage,
            int pageSize
    ) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

}
