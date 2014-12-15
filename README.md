version-maven-enforcer-rule
===========================

A maven enforcer rule set for [maven-enforcer-plugin](http://maven.apache.org/enforcer/maven-enforcer-plugin/), which can be used to restrict the project version to keep compatible with specified pattern, like OSGI version.

Enforce rule configuration
===
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>1.3.1</version>
        <dependencies>
          <dependency>
            <groupId>org.jboss.maven.plugins.enforcer.rules</groupId>
            <artifactId>version-enforcer-rule</artifactId>
            <version>0.0.1</version>
          </dependency>
        </dependencies>
        <executions>
          <execution>
            <id>enforce-osgi-version</id>
            <goals>
              <goal>enforce</goal>
            </goals>
            <configuration>
              <rules>
                <rule implementation="org.jboss.maven.plugins.enforcer.rules.version.OSGIVersionRule">
                </rule>
              </rules>
            </configuration>
          </execution>
        </executions>
    </plugin>
