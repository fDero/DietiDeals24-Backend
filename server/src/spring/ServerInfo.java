package spring;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Component @Getter
public class ServerInfo implements ApplicationListener<WebServerInitializedEvent> {

    private Integer port;
    private String address;

    @Override public void onApplicationEvent(@NotNull WebServerInitializedEvent event) {
        try {
            this.port = event.getWebServer().getPort();
            this.address = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException unknownHostException) {
            System.out.println("Server critical failure: unknown host");
            throw new RuntimeException(unknownHostException);
        }
    }
}