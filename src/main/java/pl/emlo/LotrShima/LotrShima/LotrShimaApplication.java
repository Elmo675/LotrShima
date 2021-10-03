package pl.emlo.LotrShima.LotrShima;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LotrShimaApplication implements CommandLineRunner {
    private static Logger LOG = LoggerFactory
            .getLogger(LotrShimaApplication.class);

    public static void main(String[] args) {
        LOG.info("APPLICATION START");
        SpringApplication.run(LotrShimaApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("cos robimy w grze");
    }

}
