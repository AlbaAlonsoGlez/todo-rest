package com.albaag.todo.users;

public record NewUserCommand(String username, String email, String password) {
}
