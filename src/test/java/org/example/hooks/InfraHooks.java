package org.example.hooks;

import infrastructure.process.PortWaiter;
import infrastructure.process.ProcessManager;
import infrastructure.wiremock.WireMockManager;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.example.config.TestConfig;

import java.time.Duration;

public class InfraHooks {

    // Before all and after all for global setups and cleanups
    // Focused on infra like mocking servers

    private static WireMockManager wireMockManager;
    private static ProcessManager processManager;

    @BeforeAll
    public static void startSharedInfrastructure() {
        if (!TestConfig.isMockingEnabled())
        {
            System.out.println("[INFO] Mocking disabled. Skipping Infra setup.");
            return;
        }

        processManager = new ProcessManager();
        processManager.hardResetPort(3004);

        wireMockManager = new WireMockManager(3004, new PortWaiter());
        wireMockManager.start();

        new PortWaiter().waitForPortToOpen(3004, Duration.ofSeconds(10));
        wireMockManager.stubWithAuthValidatedResponse200();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for WireMock", e);
        }
    }

    @AfterAll
    public static void stopSharedInfrastructure() {
        if (TestConfig.isMockingEnabled() && wireMockManager != null) {
            System.out.println("[INFO] Shutting down WireMock...");
            wireMockManager.stop();
        }
    }
}
