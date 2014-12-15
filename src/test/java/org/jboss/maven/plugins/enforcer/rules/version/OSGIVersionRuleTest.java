package org.jboss.maven.plugins.enforcer.rules.version;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugins.enforcer.EnforcerTestUtils;
import org.apache.maven.plugins.enforcer.MockProject;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusTestCase;
import org.junit.Test;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

/**
 * Tests on OSGIVersionRule.
 * 
 * @author <a href="mailto:lgao@redhat.com">Lin Gao</a>
 *
 */
public class OSGIVersionRuleTest extends PlexusTestCase
{
   private OSGIVersionRule rule = new OSGIVersionRule();


   private MavenProject createMavenProject(String version) throws Exception
   {
      MavenProject project = new MockProject();
      project.setVersion(version);
      return project;
   }
   
   /**
    * Valid OSGI project version tests.
    * 
    */
   @Test
   public void testOSGIValidVersions() throws Exception
   {
      MavenProject project = createMavenProject("1");
      EnforcerRuleHelper helper = EnforcerTestUtils.getHelper(project);
      rule.execute(helper);

      project = createMavenProject("1.0");
      helper = EnforcerTestUtils.getHelper(project);
      rule.execute(helper);

      project = createMavenProject("1.0.0");
      helper = EnforcerTestUtils.getHelper(project);
      rule.execute(helper);

      project = createMavenProject("1.0.0.beta");
      helper = EnforcerTestUtils.getHelper(project);
      rule.execute(helper);
      
      project = createMavenProject("1.0.0.20141214");
      helper = EnforcerTestUtils.getHelper(project);
      rule.execute(helper);
      
      project = createMavenProject("1.0.0.20141214-beta-very-long-qualifier");
      helper = EnforcerTestUtils.getHelper(project);
      rule.execute(helper);

      project = createMavenProject("1.0.0.beta-20141214");
      helper = EnforcerTestUtils.getHelper(project);
      rule.execute(helper);
   }
   
   
   /**
    * In-Valid OSGI project version tests.
    * 
    */
   @Test
   public void testOSGIIbValidVersions() throws Exception
   {
      MavenProject project = createMavenProject("1.0-beta");
      EnforcerRuleHelper helper = EnforcerTestUtils.getHelper(project);
      try
      {
         rule.execute(helper);
         fail("No way to go here.");
      }
      catch (EnforcerRuleException e) 
      {
         ;
      }
      
      project = createMavenProject("1.0.beta");
      helper = EnforcerTestUtils.getHelper(project);
      try
      {
         rule.execute(helper);
         fail("No way to go here.");
      }
      catch (EnforcerRuleException e) 
      {
         ;
      }
      
      project = createMavenProject("1.beta");
      helper = EnforcerTestUtils.getHelper(project);
      try
      {
         rule.execute(helper);
         fail("No way to go here.");
      }
      catch (EnforcerRuleException e) 
      {
         ;
      }

      project = createMavenProject("1-beta");
      helper = EnforcerTestUtils.getHelper(project);
      try
      {
         rule.execute(helper);
         fail("No way to go here.");
      }
      catch (EnforcerRuleException e) 
      {
         ;
      }

      project = createMavenProject("1.0.0-beta");
      helper = EnforcerTestUtils.getHelper(project);
      try
      {
         rule.execute(helper);
         fail("No way to go here.");
      }
      catch (EnforcerRuleException e) 
      {
         ;
      }
   }


}
