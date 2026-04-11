package infrastructure.process;

import java.time.Duration;
import java.util.Optional;

public class ProcessManager {

    private final PortProcessFinder portProcessFinder;
    private final ProcessKiller processKiller;
    private final PortWaiter portWaiter;

    public ProcessManager() {
        this.portProcessFinder = new PortProcessFinder();
        this.processKiller = new ProcessKiller();
        this.portWaiter = new PortWaiter();
    }

    /**
     * Kills the process running on the given port if it exists.
     */
    public void killProcessOnPort(int port) {
        Optional<Integer> pid = portProcessFinder.findPidByPort(port);
        pid.ifPresent(processKiller::killByPid);
    }

    /**
     * Ensures a port is free before continuing.
     */
    public void ensurePortIsFree(int port, Duration timeout) {
        killProcessOnPort(port);
        portWaiter.waitForPortToClose(port, timeout);
    }

    /**
     * Hard reset strategy:
     *  - kill process on port
     *  - wait until port is released
     */
    public void hardResetPort(int port) {
        ensurePortIsFree(port, Duration.ofSeconds(30));
    }
}
