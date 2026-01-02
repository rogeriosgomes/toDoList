package br.com.inovatte.todolist.filter;


import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.inovatte.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")) {


            //Pegar a autorização (Usuario e senha)
            var authorization = request.getHeader("Authorization");
            var authEncoder = authorization.substring("Basic ".length()).trim();

            byte[] authDecoder = Base64.getDecoder().decode(authEncoder);

            var authString = new String(authDecoder);

            String[] cedentials = authString.split(":");
            String username = cedentials[0];
            String password = cedentials[1];

            System.out.println("Authorization");
            System.out.println(username);
            System.out.println(password);


            //Validar usuário

            var user = repository.findByUsername(username);

            if (user == null) {
                response.sendError(401);
            } else {
                //Valida senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);

                } else {
                    response.sendError(401);
                }
                //Segue viagem

            }

        }else {

            filterChain.doFilter(request, response);
        }

    }


}
