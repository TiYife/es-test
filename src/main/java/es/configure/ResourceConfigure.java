package es.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ResourceConfigure extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/templates/assets/");

//        registry.addResourceHandler("/preView/**")
//                .addResourceLocations("file:"+Constant.ABSOLUTE_PATH+"preView/");
//
//        registry.addResourceHandler("/icon/**")
//                .addResourceLocations("file:"+Constant.ABSOLUTE_PATH+"icon/");
//        registry.addResourceHandler("/temp/**")
//
//                .addResourceLocations("file:"+Constant.ABSOLUTE_PATH+"temp/");
        super.addResourceHandlers(registry);
    }
}