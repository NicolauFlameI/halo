package br.com.nicolas.loglab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Ponto de entrada do LogLab, gerador de logs que simula falhas reais para o HALO analisar. */
@SpringBootApplication
public class LogLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogLabApplication.class, args);
    }
}
