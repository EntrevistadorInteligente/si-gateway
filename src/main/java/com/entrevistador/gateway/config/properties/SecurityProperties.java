package com.entrevistador.gateway.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Getter
@Setter
@ConfigurationProperties("application.security")
public class SecurityProperties {
    private Set<String> whiteList;

    public String[] getWhiteListArray() {

        if (CollectionUtils.isEmpty(whiteList)) return new String[0];

        return whiteList.stream().filter(StringUtils::isNotBlank).toArray(String[]::new);
    }
}
