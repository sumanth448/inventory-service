package com.paytm.inventory.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableCaching
@EnableJpaAuditing
@EnableTransactionManagement
public class AppConfig {
}

