package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandLineController {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	
	@GetMapping("/getDockerContent")
	public String getFileContent(@RequestParam("cliArgs") String cliArguments) {
		try {
			
			return new String(Files.readAllBytes(Paths.get(cliArguments))).trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "FAILURE";
        }
	}	
	
}
