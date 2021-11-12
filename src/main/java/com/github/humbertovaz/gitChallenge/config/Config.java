package com.github.humbertovaz.gitChallenge.config;

import com.github.humbertovaz.gitChallenge.services.LocalCommitLoader;
import com.github.humbertovaz.gitChallenge.services.LocalCommitProcessor;
import com.github.humbertovaz.gitChallenge.services.BashExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class Config {

    @Bean("bashExecutor")
    public BashExecutor bashExecutor(){
        return new BashExecutor();
    }

    @Bean("commitLoader")
    @DependsOn("bashExecutor")
    public LocalCommitLoader commitLoader(){
        return new LocalCommitLoader();
    }

    @Bean("commitProcessor")
    @DependsOn("commitLoader")
    public LocalCommitProcessor commitProcessor(){
        return new LocalCommitProcessor();
    }

}