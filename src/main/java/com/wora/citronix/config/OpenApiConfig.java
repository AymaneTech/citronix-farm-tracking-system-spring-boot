package com.wora.citronix.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(OpenApiConfigurationProperties.class)
public class OpenApiConfig {

    @Bean
    public OpenAPI defineOpenApi(OpenApiConfigurationProperties props){
        Server server = new Server();
        server.setUrl(props.server().url());
        server.setDescription(props.server().description());

        Contact contact = new Contact();
        contact.setName(props.info().contact().name());
        contact.setEmail(props.info().contact().email());
        contact.setUrl(props.info().contact().url());

        Info info = new Info()
                .title(props.info().title())
                .version(props.info().version())
                .description(props.info().description())
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));
    }

}
