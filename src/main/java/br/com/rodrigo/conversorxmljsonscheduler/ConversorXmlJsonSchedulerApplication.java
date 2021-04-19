package br.com.rodrigo.conversorxmljsonscheduler;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ConversorXmlJsonSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConversorXmlJsonSchedulerApplication.class, args);
    }

}
