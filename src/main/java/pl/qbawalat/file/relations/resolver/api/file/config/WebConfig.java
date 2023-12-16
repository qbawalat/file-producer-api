package pl.qbawalat.file.relations.resolver.api.file.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.qbawalat.file.relations.resolver.api.common.logger.LogInterceptor;
import pl.qbawalat.file.relations.resolver.api.file.converter.SortDirectionConverter;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortDirectionConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }
}
