package pl.emlo.LotrShima.LotrShima;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LotrShimaApplication implements CommandLineRunner {
    private static void print(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        print("APPLICATION START");
        SpringApplication.run(LotrShimaApplication.class, args);
        print("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        print("cos robimy w grze");
    }

}
