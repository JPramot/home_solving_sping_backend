package com.spring.home_solver.exception;

public class NotFoundExc extends RuntimeException{

    private String resource;

    private String field;

    private String fieldValue;

    private long fieldId;

    public NotFoundExc() {
    }

    public NotFoundExc(String resource, String field, String fieldValue) {
        super(String.format("%s not found with %s: %s",resource,field,fieldValue));
        this.resource = resource;
        this.fieldValue = fieldValue;
        this.field = field;
    }

    public NotFoundExc(String resource, String field, long fieldId) {
        super(String.format("%s not found with %s: %d",resource,field,fieldId));
        this.resource = resource;
        this.field = field;
        this.fieldId = fieldId;
    }

    public NotFoundExc(String message) {
        super(message);
    }
}
