package infrastructure.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

public class PortProcessFinder {

    public Optional<Integer> findPidByPort(int port) {
        try {
            Process process = new ProcessBuilder(
                    "bash",
                    "-c",
                    "lsof -ti tcp:" + port
            ).start();

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                String line = reader.readLine();
                if (line != null && !line.isBlank()) {
                    return Optional.of(Integer.parseInt(line.trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find PID for port " + port, e);
        }

        return Optional.empty();
    }
}

