package com.wora.citronix.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("app.open-api")
public record OpenApiConfigurationProperties(
        Info info,
        Server server
) {
    public record Server(
            @DefaultValue("http://localhost:8080")
            String url,

            @DefaultValue("Waiting room management module")
            String description
    ) {
    }

    public record Contact(
            @DefaultValue("Aymane El Maini")
            String name,

            @DefaultValue("elmainiaymane03@gmail.com")
            String email,

            @DefaultValue("https://aymaneelmaini.vercel.app")
            String url
    ) {
    }

    public record Info(
            @DefaultValue("Waiting room management module")
            String title,

            @DefaultValue("1.0.0")
            String version,

            @DefaultValue("Waiting room management module")
            String description,
            Contact contact
    ) {
    }
}
