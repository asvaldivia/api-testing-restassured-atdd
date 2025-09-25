package org.example;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = "org.example.stepdefinitions",
        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber.json",
                "html:target/cucumber-html-reports/report.html",
                "junit:target/junit-reports/junit.xml"
        },
        monochrome = true,
        publish = true
)

public class RunCukesTest {
}
