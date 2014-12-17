package org.jboss.maven.plugins.enforcer.rules.version;

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

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.osgi.framework.Version;

/**
 * OSGIVersionRule restrict that the project version must follow OSGI version specification.
 * 
 * See Javadoc: http://www.osgi.org/javadoc/r6/core/org/osgi/framework/Version.html#Version(java.lang.String)
 * or Section 3.2.5 in OSGI core specification v 4.2
 * 
 * @author <a href="mailto:lgao@redhat.com">Lin Gao</a>
 */
public class OSGIVersionRule implements EnforcerRule
{

   /**
    * Entry point for OSGI version rule
    *
    * @param helper
    *            the EnforcerRuleHelper
    * @throws EnforcerRuleException
    *             any exception during rule checking
    */

   public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException
   {
      MavenProject project = null;
      try
      {
         project = (MavenProject) helper.evaluate("${project}");
      }
      catch (ExpressionEvaluationException eee)
      {
         throw new EnforcerRuleException("Unable to retrieve the MavenProject: ", eee);
      }
      String version = project.getVersion();
      if (isSnapshort(version)) {
         helper.getLog().debug("Ignore SNAPSHOT version checking.");
      }
      else if (!isOSGIVersion(version, helper.getLog()))
      {
         throw new EnforcerRuleException("Version of module " + project.getName() + ": [" + version + "] is not a valid OSGI version.");
      }
   }

   private boolean isSnapshort(String version)
   {
      return version.trim().endsWith("SNAPSHOT") || version.trim().endsWith("LATEST");
   }

   private boolean isOSGIVersion(String version, Log logger)
   {
      try
      {
         Version.parseVersion(version);
         return true;
      }
      catch (Exception e)
      {
         logger.debug(e.getLocalizedMessage()); // print out for debug the reason
      }
      return false;
   }

   /**
    * Not cacheable.
    * 
    * @return false.
    */
   public boolean isCacheable()
   {
      return false;
   }

   /**
    * Not used.
    * 
    * @param er
    *            the EnforcerRule
    * @return false
    */
   public boolean isResultValid(EnforcerRule er)
   {
      return false;
   }

   /**
    * Not used.
    * 
    * @return "0"
    */
   public String getCacheId()
   {
      return "0";
   }

}
