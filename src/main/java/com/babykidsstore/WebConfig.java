package com.babykidsstore;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 1. Project లోపల ఉన్న పాత ఇమేజ్ ల కోసం పాత్
        String oldStaticPath = "file:src/main/resources/static/uploads/";

        // 2. లైవ్/లోకల్ లో కొత్తగా సేవ్ అయ్యే ఇమేజ్ ల కోసం డైనమిక్ పాత్
        String projectRoot = System.getProperty("user.dir");
        String newExternalPath = "file:" + projectRoot + File.separator + "external-uploads" + File.separator;
        newExternalPath = newExternalPath.replace("\\", "/");

        // బ్రౌజర్ లో /uploads/ అని అడిగినప్పుడు ఈ రెండు ఫోల్డర్లలో స్ప్రింగ్ బూట్ వెతుకుతుంది
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(oldStaticPath, newExternalPath);
    }
}