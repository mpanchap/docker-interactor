package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	
	@GetMapping("/getDockerResponse")
	public String interactWithDockerInstance(@RequestParam("cliArgs") String dockerArguments) {
	        String dirName = "commandsDir";
	        String scriptName = "script.sh";

	        File directory = new File(dirName);
	        if (!directory.exists()) {
	            directory.mkdir();  
	            System.out.println("Directory created: " + dirName);
	        } else {
	            System.out.println("Directory already exists: " + dirName);
	        }

	        try (OutputStream os = new FileOutputStream(new File(directory, scriptName))) {
	            String scriptContent = "#!/bin/bash\n" + "/usr/local/bin/" + dockerArguments;
	            os.write(scriptContent.getBytes());  // Write the script content
	            System.out.println("Shell script created: " + scriptName);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        try {
	            ProcessBuilder chmodProcess = new ProcessBuilder("chmod", "+x", directory + "/" + scriptName);
	            chmodProcess.inheritIO();  // Inherit I/O to see output in console
	            Process chmod = chmodProcess.start();
	            chmod.waitFor();
	            System.out.println("Made the script executable: " + scriptName);
	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	        }

	        try {
	            ProcessBuilder processBuilder = new ProcessBuilder("bash", directory + "/" + scriptName);
	            processBuilder.inheritIO();  // Inherit I/O to see output in console
	            Process process = processBuilder.start();
	            
	            // Wait for the process to complete
	            int exitCode = process.waitFor();
	            System.out.println("Script executed with exit code: " + exitCode);
	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	        }
	        
	        finally {
	        	// I will have to remove the script and directories
	        }
	        
	    return "SUCCESS";
	}
	
	
	@PostMapping("/interactWithDocker")
	public String interact(@RequestBody String[] arguments) {
	        String dirName = "commandsDir";
	        String scriptName = "script.sh";

	        File directory = new File(dirName);
	        if (!directory.exists()) {
	            directory.mkdir();  
	            System.out.println("Directory created: " + dirName);
	        } else {
	            System.out.println("Directory already exists: " + dirName);
	        }
	        
	        
	        Stream<String> stream = Arrays.stream(arguments); 

	        
	        stream.forEach(input -> {
	        	
	        	try (OutputStream os = new FileOutputStream(new File(directory, scriptName))) {
		            String scriptContent = "#!/bin/bash\n" + "/usr/local/bin/" + input;
		            os.write(scriptContent.getBytes());  // Write the script content
		            System.out.println("Shell script created: " + scriptName);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		        try {
		            ProcessBuilder chmodProcess = new ProcessBuilder("chmod", "+x", directory + "/" + scriptName);
		            chmodProcess.inheritIO();  // Inherit I/O to see output in console
		            Process chmod = chmodProcess.start();
		            chmod.waitFor();
		            System.out.println("Made the script executable: " + scriptName);
		        } catch (IOException | InterruptedException e) {
		            e.printStackTrace();
		        }

		        try {
		            ProcessBuilder processBuilder = new ProcessBuilder("bash", directory + "/" + scriptName);
		            processBuilder.inheritIO();  // Inherit I/O to see output in console
		            Process process = processBuilder.start();
		            
		            // Wait for the process to complete
		            int exitCode = process.waitFor();
		            System.out.println("Script executed with exit code: " + exitCode);
		        } catch (IOException | InterruptedException e) {
		            e.printStackTrace();
		        }
		        
		        finally {
		        	// I disabled this to run unit tests
//		        	try {
//						deleteDirectory(directory.toPath());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
		        }
	        	
	        });
	        
	    return "SUCCESS";
	}
	
	public static void deleteDirectory(Path path) throws IOException {
        // Check if the directory exists
        if (!Files.exists(path)) {
            throw new IOException("Directory does not exist: " + path);
        }

        // Check if the path is a directory
        if (!Files.isDirectory(path)) {
            throw new IOException("Path is not a directory: " + path);
        }

        // Delete all files and subdirectories in the directory
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    // Recursively delete subdirectory
                    deleteDirectory(entry);
                } else {
                    // Delete file
                    Files.delete(entry);
                }
            }
        }

        // Finally, delete the empty directory
        Files.delete(path);
    }
}
