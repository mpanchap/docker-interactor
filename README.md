Functional requirements:
- An application that should be able to run command line arguments will interact with the Docker instance the application is deployed/running.
- Retrieve contents of a specified file inside the docker instance. 


Non-functional requirements:
- As I should consider scenarios like Parallelization and Efficiency, I am going to have a simple SpringBoot Java Application running in the docker instance, which will address the parallelization overhead as Java can handle CPU-heavy computations. 
- I can simply compile a python file or a java file inside the docker instance and start the docker instance but instead, I will attempt to expose this spring boot application in a port 8080 so that it can be accessible from browser and provide an API rich interaction. 
- Also, the simple REST API will allow to take the command line arguments and process the commands in the background, making it reusable if I could persist those frequently used commands. It will give us room to do some massaging on the input commands later on and retrieve the files and fail gracefully in case of error scenarios. 


To retrieve contents inside the file using docker command in the application, I will retrieve the docker logs itself and see if I can return it successfully as an API response. 


Pre requisites in the local machine: 

Docker CLI or App
Java 17 
Apache maven 3.9.9

<img width="835" alt="image" src="https://github.com/user-attachments/assets/d1c61c85-a7d7-41bf-afd8-30f8b6f43d85">

Simple GET API to get the container id 

```
curl --location --request GET 'http://localhost:8081/getDockerResponse?cliArgs=docker ps'
```

API to run docker commands simultaneously

```
curl --location --request POST 'http://localhost:8080/interactWithDocker' \
--header 'Content-Type: application/json' \
--data-raw '["docker ps", "docker ps -a", "docker images"]
'
```


API to fetch files, folders 

```
curl --location --request POST 'http://localhost:8081/interactWithDocker' \
--header 'Content-Type: application/json' \
--data-raw '["docker cp ab36bd398d8b:/app /Users/manojkpanchapakesan/appContent"]'
```

API to fetch file contents

```
curl --location --request POST 'http://localhost:8081/fetchContentsFromDocker' \
--header 'Content-Type: application/json' \
--data-raw '["ab36bd398d8b", "/app"]
'
```
