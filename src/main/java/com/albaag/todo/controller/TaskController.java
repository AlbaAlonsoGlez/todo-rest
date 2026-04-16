package com.albaag.todo.controller;

import com.albaag.todo.dto.EditTaskCommand;
import com.albaag.todo.dto.GetTaskDto;
import com.albaag.todo.model.Task;
import com.albaag.todo.service.TaskService;
import com.albaag.todo.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task/")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(
            summary = "Obtener todas las tareas del usuario",
            description = "Permite obtener todas las tareas de un usuario"
    )
    @ApiResponse(description = "Listado de tareas del usuario",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = GetTaskDto.class)),
                    examples = {
                            @ExampleObject("""
                                    [
                                        {
                                             "id": 1,
                                             "title": "Hacer proyecto DWES",
                                             "description": "Terminar el proyecto de API REST de DWES.",
                                             "createdAt": "2026-01-13T09:12:11.295172",
                                             "deadline": "2026-04-17T23:59:59.000000",
                                             "author": {
                                                 "id": 1,
                                                 "username": "alba",
                                                 "email": "albaag87@educastur.es"
                                             }
                                         },
                                         {
                                             "id": 17,
                                             "title": "Entregar diplomas DWES",
                                             "description": "Entregar los diplomas de los cursos de OpenWebinars en las aulas virtuales.",
                                             "createdAt": "2026-01-13T09:12:11.295172",
                                             "deadline": "2026-04-17T23:59:59.000000",
                                             "author": {
                                                   "id": 1,
                                                   "username": "alba",
                                                   "email": "albaag87@educastur.es"
                                             }
                                         }
                                    ]
                                """)
                    }
            )
    )
    @GetMapping
    public List<GetTaskDto> getAll(
            @AuthenticationPrincipal User author
    ) {
        return taskService.findByAuthor(author)
                .stream()
                .map(GetTaskDto::of)
                .toList();
    }

    @Operation(
            summary = "Obtener una tarea concreta",
            description = "Permite obtener una tarea concreta si se le proporciona un id"
    )
    @ApiResponse(description = "Información detallada de una tarea",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetTaskDto.class),
                    examples = {
                            @ExampleObject("""
                                    {
                                             "id": 1,
                                             "title": "Hacer proyecto DWES",
                                             "description": "Terminar el proyecto de API REST de DWES.",
                                             "createdAt": "2026-01-13T09:12:11.295172",
                                             "deadline": "2026-04-17T23:59:59.000000",
                                             "author": {
                                                 "id": 1,
                                                 "username": "alba",
                                                 "email": "albaag87@educastur.es"
                                             }
                                         }
                                """)
                    }
            )
    )
    @PostAuthorize("""
            returnObject.author.username == authetication.principal.username
            """)
    @GetMapping("/{id}")
    public GetTaskDto getById(@PathVariable Long id) {
        return GetTaskDto.of(taskService.findById(id));
    }


    @Operation(
            summary = "Crear una tarea",
            description = "Permite crear una tarea asociada al usuario autenticado"
    )
    @ApiResponse(description = "Tarea recién creada",
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetTaskDto.class),
                    examples = {
                            @ExampleObject("""
                                    {
                                             "id": 1,
                                             "title": "Hacer proyecto DWES",
                                             "description": "Terminar el proyecto de API REST de DWES.",
                                             "createdAt": "2026-01-13T09:12:11.295172",
                                             "deadline": "2026-04-17T23:59:59.000000",
                                             "author": {
                                                 "id": 1,
                                                 "username": "alba",
                                                 "email": "albaag87@educastur.es"
                                             }
                                         }
                                """)
                    }
            )
    )

    @PostMapping
    public ResponseEntity<GetTaskDto> create(
            @RequestBody EditTaskCommand cmd,
            @AuthenticationPrincipal User author
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetTaskDto.of(taskService.save(cmd, author)));
    }


    @Operation(
            summary = "Editar una tarea",
            description = "Permite editar una tarea asociada al usuario autenticado si se proporciona su ID"
    )
    @ApiResponse(description = "Tarea editada",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetTaskDto.class),
                    examples = {
                            @ExampleObject("""
                                    {
                                             "id": 1,
                                             "title": "Hacer proyecto DWES",
                                             "description": "Terminar el proyecto de API REST de DWES.",
                                             "createdAt": "2026-01-13T09:12:11.295172",
                                             "deadline": "2026-04-17T23:59:59.000000",
                                             "author": {
                                                 "id": 1,
                                                 "username": "alba",
                                                 "email": "albaag87@educastur.es"
                                             }
                                         }
                                """)
                    }
            )
    )
    @PreAuthorize("""
            @ownerCheck.check(#id, authentication.principal.getId())
            """)
    @PutMapping("/{id}")
    public GetTaskDto edit(@RequestBody EditTaskCommand cmd,
                     @PathVariable Long id) {
        return GetTaskDto.of(taskService.edit(cmd, id));
    }

    @Operation(
            summary = "Eliminar una tarea",
            description = "Permite eliminar una tarea asociada al usuario autenticado si se proporciona su ID"
    )
    @ApiResponse(description = "Respuesta correcta de tarea eliminada",
            responseCode = "204",
            content = @Content(schema = @Schema(implementation = Void.class)))
    @PreAuthorize("""
            @ownerCheck.check(#id, authentication.principal.getId())
            """)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
