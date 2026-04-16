package com.albaag.todo.error;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(Long id) {
        super("No hay ninguna tarea con este IDL: %d".formatted(id));
    }

    public TaskNotFoundException() {
        super("No hay ninguna tarea con los requisitos de búsqueda indicados :(");
    }
}
