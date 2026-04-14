package infrastructure.process;

import java.io.InputStream;
import java.net.Socket;
import java.time.Duration;

public class PortWaiter {

    public PortWaiter() {
    }

    public void waitForPortToOpen(int port, Duration timeout) {
        waitForCondition(port, timeout, true);
    }

    public void waitForPortToClose(int port, Duration timeout) {
        waitForCondition(port, timeout, false);
    }

    private void waitForCondition(int port, Duration timeout, boolean shouldBeOpen) {
        long end = System.currentTimeMillis() + timeout.toMillis();

        while (System.currentTimeMillis() < end) {
            boolean open = isPortOpen(port);
            if (open == shouldBeOpen) {
                return;
            }
            sleep(200);
        }

        throw new RuntimeException(
                "Timeout waiting for port " + port + " to be " +
                        (shouldBeOpen ? "open" : "closed")
        );
    }

    private boolean isPortOpen(int port) {
        try {
            Process process = new ProcessBuilder(
                    "sh", "-c", "lsof -i :" + port
            ).start();

            try (InputStream is = process.getInputStream()) {
                return is.read() != -1;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to check port usage for port " + port, e);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
