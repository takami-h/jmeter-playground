package com.natswell.example.jmeter;

import static org.assertj.core.api.Assertions.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class HomeVisitorTest {
  @Test void someoneVisitsHome() throws Exception {
    var stats = testPlan(
        threadGroup("someone").rampTo(100, Duration.ofSeconds(5)).holdIterating(10).children(
            transaction("someone visits home and see a file",
                httpSampler("http://localhost:8080/"),
                uniformRandomTimer(1000, 1000),
                httpSampler("http://localhost:8080/pom.xml")
            )
        ),
        htmlReporter(String.format("jmeter-report-%s", Instant.now().toString().replace(":", "-")))
    ).run();
    
    assertThat(stats.overall().sampleTimePercentile99())
      .isLessThan(Duration.ofSeconds(1));
  }
}
