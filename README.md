# GitChallenge

## Pre-requisites

- Java 11
- Maven
## How to run it
``mvn clean install &&  mvn spring-boot:run``

## Purpose of the project
This project aims to collect and parse commits from remote github repository defined in application.properties file (remotecommitloader.url)

It is scattered in different components based on domain.

### CommitAggregator class
    
- Rest controller that by default is going to collect commits info from the chosen repository
- It fallbacks to a local solution using git cli


### CommitDTO class

- It is a module that is going to encapsulate the info parsed for each commit

### BashExecutor class

- Executes the passed bash command on application.properties

### LocalCommitParser class

- Parses commit from text to CommitDTO

### LocalCommitLoader class

- Loads commits gathered by git cli command to memory

### LocalCommitProcessor class

- Service called by CommitAggregator class that calls LocalCommitLoader

### RemoteCommitLoader class
 
- Loads commits gathered by a GET request to git remote api to memory

### RemoteCommitParser class

- Parses commit from json to CommitDTO

### RemoteCommitProcessor class

- Service called by CommitAggregator class that calls RemoteCommitLoader

### DataCluster class

- Wrapper class encapsulating InputStream and BufferedReader

### PagingUtils class

- Class with several paging utilities