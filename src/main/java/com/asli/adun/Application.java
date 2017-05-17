package com.asli.adun;

import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication 
public class Application extends SpringBootServletInitializer {
    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(Application.class);
        if (hasNoActiveProfileOfNameOverrides()) {
            springApplication.setAdditionalProfiles("overrides");
        }
        springApplication.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        if (hasNoActiveProfileOfNameOverrides()) {
            application.profiles("overrides");
        }
        return application.sources(Application.class);
    }

    private static boolean hasNoActiveProfileOfNameOverrides() {

        String activeProfiles = Optional.ofNullable(System.getProperty("spring.profiles.active")).orElse("");
        Pattern pattern = Pattern.compile("[\\.+,]*overrides[,\\.+]*");

        return !pattern.matcher(activeProfiles).find();
    }
}
