/*
 * Copyright 2007-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxchess.jcpi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionInfo {

  private static final String versionInfoProperty = "/com/fluxchess/jcpi/version-info.properties";
  private static final VersionInfo CURRENT;

  private final String version;
  private final String buildNumber;
  private final String revisionNumber;

  static {
    InputStream inputStream = VersionInfo.class.getResourceAsStream(versionInfoProperty);
    try {
      if (inputStream != null) {
        Properties properties = new Properties();
        properties.load(inputStream);

        CURRENT = new VersionInfo(
          properties.getProperty("version", "n/a"),
          properties.getProperty("buildNumber", "n/a"),
          properties.getProperty("revisionNumber", "n/a")
        );
      } else {
        throw new FileNotFoundException(String.format("Cannot find the properties file %s", versionInfoProperty));
      }
    } catch (IOException e) {
      throw new RuntimeException(String.format("Cannot load the properties file %s", versionInfoProperty), e);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          // Do nothing
        }
      }
    }
  }

  public static VersionInfo current() {
    return CURRENT;
  }

  private VersionInfo(String version, String buildNumber, String revisionNumber) {
    if (version == null) throw new IllegalArgumentException();
    if (buildNumber == null) throw new IllegalArgumentException();
    if (revisionNumber == null) throw new IllegalArgumentException();

    this.version = version;
    this.buildNumber = buildNumber;
    this.revisionNumber = revisionNumber;
  }

  @Override
  public String toString() {
    return String.format("JCPI %s", version);
  }

  public String getVersion() {
    return version;
  }

  public String getBuildNumber() {
    return buildNumber;
  }

  public String getRevisionNumber() {
    return revisionNumber;
  }

}
