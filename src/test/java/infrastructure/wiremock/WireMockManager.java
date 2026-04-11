package infrastructure.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import infrastructure.process.PortWaiter;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockManager {

    private final int port;
    private final PortWaiter portWaiter;

    private WireMockServer wireMockServer;

    public WireMockManager(int port, PortWaiter portWaiter) {
        this.port = port;
        this.portWaiter = portWaiter;
    }

    public void start() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            return;
        }

        wireMockServer = new WireMockServer(options().port(port).bindAddress("127.0.0.1"));
        wireMockServer.start();

        portWaiter.waitForPortToOpen(port, Duration.ofSeconds(120));
        configureFor("127.0.0.1", port);
    }

    public void stop() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            portWaiter.waitForPortToClose(port, Duration.ofSeconds(120));
            wireMockServer = null;
        }
    }


    public void reset() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.resetAll();
        }
    }

    // -------------------------
    // Auth stubbing strategies
    // -------------------------

    public void stubSetTokenAuthenticatedUser() {
        stubFor(post(urlEqualTo("/auth/validate"))
                .withRequestBody(equalToJson("{ \"token\": \"abc123\" }"))
                .willReturn(aResponse()
                        .withStatus(200)));
    }

    public void stubWithAuthValidatedResponse200() {
        stubFor(post("/auth/validate")
                .willReturn(aResponse().withStatus(200)));
    }
}

