package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EntityScan(basePackages = {"entity"})
@EnableJpaRepositories(basePackages = {"repository"})
@ComponentScan(basePackages = {"spring","service","controller", "authentication", "utils"})
@EnableAsync
public class ServerStartup {

    private static final Logger logger = LoggerFactory.getLogger(ServerStartup.class);
    public static void main(String[] args) {
        var context = SpringApplication.run(ServerStartup.class, args);
        Integer port = context.getBean(ServerInfo.class).getPort();
        String address = context.getBean(ServerInfo.class).getAddress();
        logger.info("Server startup success!");
        logger.info("operating with ip: \"{}\"", address);
        logger.info("operating on port: \"{}\"", port);
    }
}
