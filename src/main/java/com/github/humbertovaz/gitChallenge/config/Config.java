package com.github.humbertovaz.gitChallenge.config;

import com.github.humbertovaz.gitChallenge.services.*;
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

    @Bean("localCommitProcessor")
    @DependsOn("commitLoader")
    public LocalCommitProcessor localCommitProcessor(){
        return new LocalCommitProcessor();
    }

    @Bean("remoteCommitProcessor")
    public RemoteCommitProcessor remoteCommitProcessor(){
        return new RemoteCommitProcessor();
    }

    @Bean("remoteCommitLoader")
    public RemoteCommitLoader remoteCommitLoader(){
        return new RemoteCommitLoader();
    }


}