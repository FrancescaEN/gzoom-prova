package it.mapsgroup.gzoom.configuration.spring;

import it.mapsgroup.gzoom.common.LanguageType;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties
public class LanguageConfiguration {

    @Value("${language.multi.type:NONE}")
    private LanguageType languageType;
    @Value("${language.locales.available}")
    private List<String> availableLanguages;
}
