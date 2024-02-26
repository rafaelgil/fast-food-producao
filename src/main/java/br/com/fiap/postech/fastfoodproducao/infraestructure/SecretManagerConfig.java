package br.com.fiap.postech.fastfoodproducao.infraestructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
@Profile("!local")
public class SecretManagerConfig {

    @Bean
    public SecretsManagerClient getSecretsManagerClient() {
        return SecretsManagerClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
