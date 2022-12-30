package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class EntryResponseMessage<T> {
    private List<Employee> submitted;
    private T data;
    private List<Employee> pending;

    public EntryResponseMessage(List<Employee> submitted, List<Employee> pending) {
        this.submitted = submitted;
        this.pending = pending;
    }

    public EntryResponseMessage(List<Employee> submitted, T data, List<Employee> pending) {
        this.submitted = submitted;
        this.data = data;
        this.pending = pending;
    }

    public List<Employee> getSubmitted() {
        return submitted;
    }

    public void setSubmitted(List<Employee> submitted) {
        this.submitted = submitted;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Employee> getPending() {
        return pending;
    }

    public void setPending(List<Employee> pending) {
        this.pending = pending;
    }
}
