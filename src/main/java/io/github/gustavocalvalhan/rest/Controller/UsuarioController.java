package io.github.gustavocalvalhan.rest.Controller;

import io.github.gustavocalvalhan.domain.entity.Usuario;
import io.github.gustavocalvalhan.exception.SenhaInvalidaException;
import io.github.gustavocalvalhan.rest.dto.CredenciaisDTO;
import io.github.gustavocalvalhan.rest.dto.TokenDTO;
import io.github.gustavocalvalhan.security.jwt.JwtService;
import io.github.gustavocalvalhan.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioServiceImpl usuarioService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);

    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credencias) {
        try {
            Usuario usuario = Usuario.builder()
                    .login(credencias.getLogin())
                    .senha(credencias.getSenha()).build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        }

    }

