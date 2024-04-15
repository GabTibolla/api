package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class UsersController {

    private final UsersRepository usuarioRepository;
    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usuarioRepository, UsersRepository usersRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usersRepository = usersRepository;
    }

    // Função Get (Por ID) - Usuário
    @GetMapping("/usuarios/{id}")
    public ResponseEntity getUsuario(@PathVariable("id") Integer id) {
        UsersModel user = usuarioRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);

    }

    // Função Get (Todos) - Usuário
    @GetMapping("/usuarios")
    public List<UsersModel> getUsuarios() {
        Iterable<UsersModel> usersInterable = usuarioRepository.findAll();
        List<UsersModel> users = new ArrayList<>();

        usersInterable.forEach(users::add);

        return users;
    }

    // Função PUT - Usuário
    @PutMapping(path = "/usuarios/{id}")
    public ResponseEntity<?> updateUsuario(@RequestBody UsersModel usersModel, @PathVariable("id") Integer id) {
        UsersModel user = usuarioRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));

        }

        if (usersModel.getName() != null) {
            user.setName(usersModel.getName());
        }
        if (usersModel.getPermission() != null){
            user.setPermission(usersModel.getPermission());
        }

        // Atualiza a hora de edição do usuário
        user.setUpdatedAt(LocalDateTime.now());

        usersModel = usersRepository.save(user);

        return ResponseEntity.ok().body(usersModel);
    }

    // Função POST - Usuário
    @PostMapping(path = "/usuarios")
    public ResponseEntity<?> insertUser(@RequestBody UsersModel users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(users));
    }

    // Função DELETE - Usuário
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") Integer id) {
        // Verifica se o usuário existe
        UsersModel user = usuarioRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));

        }

        usuarioRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body(("{\"message\": \"Usuário excluído com sucesso\" }"));
    }
}
