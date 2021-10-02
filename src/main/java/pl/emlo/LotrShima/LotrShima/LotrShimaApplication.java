package pl.emlo.LotrShima.LotrShima;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LotrShimaApplication implements CommandLineRunner {
    private static Logger LOG = LoggerFactory
            .getLogger(LotrShimaApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION ffffffffffffffffuuuuuuuuuuuuuuuuuu");
        SpringApplication.run(LotrShimaApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");
        Scanner scanner = new Scanner(System.in);
        String x;
        x = scanner.nextLine();
        LOG.info(x);
    }

}
