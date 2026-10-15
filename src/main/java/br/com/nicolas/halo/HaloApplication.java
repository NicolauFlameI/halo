package br.com.nicolas.halo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada do HALO (Host Analysis & Log Observer).
 *
 * <p>{@code @SpringBootApplication} ativa três coisas de uma vez:
 * configuração automática do Spring Boot, varredura de componentes
 * (controllers, services, repositories) no pacote {@code br.com.nicolas.halo}
 * e seus subpacotes, e a classe como fonte de configuração.</p>
 */
@SpringBootApplication
public class HaloApplication {

    /** Inicia o servidor web embutido (Tomcat) e carrega todo o contexto da aplicação. */
    public static void main(String[] args) {
        SpringApplication.run(HaloApplication.class, args);
    }
}
