package org.marehman.rideSharing.utils;

import org.springframework.http.HttpStatus;

public class Response {
    private HttpStatus status;
    private Object message;
    private int page;
    private Long count;
    private int totalPages;

    public Response(HttpStatus status, Object message) {
        this.status = status;
        this.message = message;
    }

    public Response() {
    }

    public Response(HttpStatus status, Object message, int page, Long count, int totalPages) {
        this.status = status;
        this.message = message;
        this.page = page;
        this.count = count;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
