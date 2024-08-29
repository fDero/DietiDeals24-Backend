package spring;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EntityScan(basePackages = {"entity"})
@EnableJpaRepositories(basePackages = {"repository"})
@ComponentScan(basePackages = {"spring","service","controller", "authentication"})
@EnableAsync
@EnableCaching
public class ServerStartup {

    public static void main(String[] args) {
        var context = SpringApplication.run(ServerStartup.class, args);
        Integer port = context.getBean(ServerInfo.class).getPort();
        String address = context.getBean(ServerInfo.class).getAddress();
        System.out.println("\n\nServer startup success!");
        System.out.println("operating with ip: " + address);
        System.out.println("operating on port: " + port);
    }
}
