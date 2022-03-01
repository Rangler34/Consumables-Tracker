package ca.cmpt213.a4.webappserver.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

//https://www.youtube.com/watch?v=rXBsnNCH59o Dr.Brian Fraser
//https://stackoverflow.com/questions/36119852/spring-boot-actuator-pretty-print-json

/**
 * Helps print the json nicer
 */
@Configuration
public class SpringPrintJson extends WebMvcConfigurationSupport {

    /**
     * helps with printing the json nicer
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jacksonConverter =
                        (MappingJackson2HttpMessageConverter) converter;
                jacksonConverter.setPrettyPrint(true);
            }
        }
    }
}
