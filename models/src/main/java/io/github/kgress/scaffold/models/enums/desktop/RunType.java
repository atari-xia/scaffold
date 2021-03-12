package io.github.kgress.scaffold.models.enums.desktop;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A simple enum to differentiate the many run types that Selenium can handle.
 */
@Getter
@AllArgsConstructor
public enum RunType {
  UNIT("Unit Testing"),
  LOCAL("Local"),
  GRID("Selenium Grid"),
  AWS_LAMBDA("AWS Lambda"),
  SAUCE("SauceLabs"),
  SAUCE_MOBILE_EMULATOR("SauceLabs Mobile Emulator"),
  HEADLESS("Headless");

  private final String runType;
}
