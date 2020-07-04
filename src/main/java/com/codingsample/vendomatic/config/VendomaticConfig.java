package com.codingsample.vendomatic.config;

import com.codingsample.vendomatic.model.modelone.ModelOneVendingMachine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VendomaticConfig {

    @Bean
    public ModelOneVendingMachine modelOneVendingMachine(){
        return new ModelOneVendingMachine();
    }
}
