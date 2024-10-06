import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.CommandLineController;

class DockerInteractionTest {

    private CommandLineController commandLineController; // Replace this with your class that contains the method
    
    String[] args = {"docker ps"};

    @BeforeEach
    void setUp() {
    	commandLineController = new CommandLineController();
    }

    @Test
    void testDirectoryCreation() throws Exception {
        
        String dirName = "commandsDir";
        File directory = new File(dirName);

        // Mock file system behavior
        File mockDir = mock(File.class);
        when(mockDir.exists()).thenReturn(false);  // Simulate directory not existing
        when(mockDir.mkdir()).thenReturn(true);    // Simulate directory being created

        commandLineController.interact(args);

        // Verify the directory was created
        assertTrue(directory.exists());
    }

    @Test
    void testShellScriptCreation() throws Exception {
        String scriptName = "script.sh";
        File scriptFile = new File("commandsDir/" + scriptName);

        // Invoke the method
        commandLineController.interact(args);

        // Assert the script file exists
        assertTrue(scriptFile.exists());

        // Assert the contents of the script
        String scriptContent = Files.readString(scriptFile.toPath());
        assertTrue(scriptContent.contains("#!/bin/bash"));
        assertTrue(scriptContent.contains("/usr/local/bin/echo Hello"));
    }

    @Test
    void testScriptExecution() throws Exception {
        ProcessBuilder mockProcessBuilder = mock(ProcessBuilder.class);
        Process mockProcess = mock(Process.class);

        // Simulate process execution
        when(mockProcessBuilder.start()).thenReturn(mockProcess);
        when(mockProcess.waitFor()).thenReturn(0); // Simulate successful execution

        commandLineController.interact(args);

        // Verify process was started and executed
        verify(mockProcessBuilder, times(1)).start();
    }

    @Test
    void testDirectoryCleanup() throws Exception {
        String dirName = "commandsDir";
        File directory = new File(dirName);

        // Invoke the method
        commandLineController.interact(args);

        // Verify directory was deleted after execution
        assertFalse(directory.exists());
    }
}
