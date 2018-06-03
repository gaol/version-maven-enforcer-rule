version-maven-enforcer-rule
===========================

A maven enforcer rule set for [maven-enforcer-plugin](http://maven.apache.org/enforcer/maven-enforcer-plugin/), which can be used to restrict the project version to keep compatible with specified pattern, like OSGI version.

Or limit the dependencies to following specific version patterns.

Example enforce rule configuration
===

The following example shows the way to banned all dependencies whose version does not have the string 'redhat':

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <dependencies>
          <dependency>
            <groupId>org.jboss.maven.plugins.enforcer.rules</groupId>
            <artifactId>version-enforcer-rule</artifactId>
          </dependency>
        </dependencies>
        <executions>
          <execution>
            <id>enforce-redhat-in-version</id>
            <goals>
              <goal>enforce</goal>
            </goals>
            <configuration>
              <rules>
                <rule implementation="org.jboss.maven.plugins.enforcer.rules.version.BanVersionDependenciesRule">
                  <versionPattern>^((?!redhat).)*$</versionPattern>
                </rule>
              </rules>
            </configuration>
          </execution>
        </executions>
    </plugin>
