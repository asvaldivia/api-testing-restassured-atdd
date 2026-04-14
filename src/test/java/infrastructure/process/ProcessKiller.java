package infrastructure.process;

public class ProcessKiller {

    public void killByPid(int pid) {
        try {
            new ProcessBuilder(
                    "bash",
                    "-c",
                    "kill -9 " + pid
            ).start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to kill process with PID " + pid, e);
        }
    }
}

