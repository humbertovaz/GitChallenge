package com.github.humbertovaz.gitChallenge.config;

import com.github.humbertovaz.gitChallenge.utils.CommitLoader;
import com.github.humbertovaz.gitChallenge.utils.CommitProcessor;
import com.github.humbertovaz.gitChallenge.utils.BashExecutor;
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
    public CommitLoader commitLoader(){
        return new CommitLoader();
    }

    @Bean("commitProcessor")
    @DependsOn("commitLoader")
    public CommitProcessor commitProcessor(){
        return new CommitProcessor();
    }

}