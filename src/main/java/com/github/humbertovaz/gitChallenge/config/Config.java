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

    @Bean("localCommitLoader")
    @DependsOn("bashExecutor")
    public LocalCommitLoader localCommitLoader(){
        return new LocalCommitLoader();
    }

    @Bean("localCommitProcessor")
    @DependsOn("localCommitLoader")
    public LocalCommitProcessor localCommitProcessor(){
        return new LocalCommitProcessor();
    }

    @Bean("remoteCommitLoader")
    public RemoteCommitLoader remoteCommitLoader(){
        return new RemoteCommitLoader();
    }

    @Bean("remoteCommitProcessor")
    @DependsOn("remoteCommitLoader")
    public RemoteCommitProcessor remoteCommitProcessor(){
        return new RemoteCommitProcessor();
    }
}