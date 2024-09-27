package spring;

import exceptions.UnexpectedHostException;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component @Getter
public class ServerInfo implements ApplicationListener<WebServerInitializedEvent> {

    private Integer port;
    private String address;

    @Override public void onApplicationEvent(@NonNull WebServerInitializedEvent event) {
        try {
            this.port = event.getWebServer().getPort();
            this.address = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException unknownHostException) {
            throw new UnexpectedHostException();
        }
    }
}