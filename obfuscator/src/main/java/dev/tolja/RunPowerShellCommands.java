package dev.tolja;

import java.io.IOException;

public class RunPowerShellCommands {
    public static void RunCommands() {
        String directoryPath = "out\\cpp";

        String[] commands = {
                "powershell.exe",
                "-Command",
                "Set-Location -Path '" + directoryPath + "'; cmake ."
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("CMake configure succeeded.");
            } else {
                System.err.println("CMake configure failed. Exit code: " + exitCode);
            }

            String[] buildCommands = {
                    "powershell.exe",
                    "-Command",
                    "Set-Location -Path '" + directoryPath + "'; cmake --build . --config Release"
            };

            ProcessBuilder buildProcessBuilder = new ProcessBuilder(buildCommands);
            Process buildProcess = buildProcessBuilder.start();

            int buildExitCode = buildProcess.waitFor();
            if (buildExitCode == 0) {
                System.out.println("CMake build succeeded.");
            } else {
                System.err.println("CMake build failed. Exit code: " + buildExitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}