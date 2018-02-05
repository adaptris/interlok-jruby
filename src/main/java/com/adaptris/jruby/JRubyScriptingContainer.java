/*
 * Copyright 2017 Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adaptris.jruby;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.jruby.embed.ScriptingContainer;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.DynamicPollingTemplate;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.ExceptionHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Execute a jruby script using {@code JRuby Embed Core} Scripting container.
 * 
 * <p>
 * Rather than making use of the JSR223 bindings where things need to be controlled via system properties; this makes use
 * of {@code ScriptingContainer} to execute your jruby scripts which allows more fine-grained control over the behaviour.
 * </p>
 * <p>
 * In addition to specifying the script that works on the {@link AdaptrisMessage} object; you have the option of specifying scripts
 * that should be executed as each part of the lifecycle phases {@code init(), start(), stop(), close()}.
 * </p>
 * <p>
 * The {@code service-script} will executed at runtime and the AdaptrisMessage that is due to be processed is bound as a variable
 * {@code message}. All executed scripts will have an instance of org.slf4j.Logger is also bound as {@code log}. These can be used
 * as a standard variables within the script though of course you may need additional qualifiers if you specify a different
 * {@link ContainerBuilderImpl#getVariableBehaviour()}.
 * </p>
 * 
 * @author lchan
 * @config jruby-scripting-service
 * 
 */
@XStreamAlias("jruby-scripting-service")
@AdapterComponent
@ComponentProfile(summary = "Execute a JRuby script stored on the filesystem/classpath", tag = "service,scripting",
    branchSelector = true)
@DisplayOrder(order = {"branchingEnabled", "serviceScript", "initScript", "startScript", "stopScript", "closeScript"})
public class JRubyScriptingContainer extends ServiceImp implements DynamicPollingTemplate.TemplateProvider {

  @InputFieldDefault(value = "false")
  private Boolean branchingEnabled;
  @Valid
  private ScriptWrapper serviceScript;
  @NotNull
  @Valid
  @AutoPopulated
  private ContainerBuilder builder;

  @AdvancedConfig
  @Valid
  private ScriptWrapper initScript;
  @AdvancedConfig
  @Valid
  private ScriptWrapper startScript;
  @AdvancedConfig
  @Valid
  private ScriptWrapper stopScript;
  @AdvancedConfig
  @Valid
  private ScriptWrapper closeScript;


  public JRubyScriptingContainer() {
    super();
    setBuilder(new DefaultBuilder());
  }

  @Override
  public final void doService(AdaptrisMessage msg) throws ServiceException {
    try {
      ScriptingContainer container = getBuilder().build();
      container.put("message", msg);
      execute(container, getServiceScript());
    } catch (CoreException e) {
      throw ExceptionHelper.wrapServiceException(e);
    }
  }

  @Override
  protected void initService() throws CoreException {
    execute(getBuilder().build(), getInitScript());
  }

  @Override
  protected void closeService() {
    executeQuietly(getCloseScript());
    destroy();
  }


  @Override
  public void start() throws CoreException {
    execute(getBuilder().build(), getStartScript());
    super.start();
  }

  @Override
  public void stop() {
    executeQuietly(getStopScript());
    super.stop();
  }

  @Override
  public void prepare() throws CoreException {}


  @Override
  public boolean isBranching() {
    return getBranchingEnabled() != null ? getBranchingEnabled().booleanValue() : false;
  }

  public Boolean getBranchingEnabled() {
    return branchingEnabled;
  }

  /**
   * Specify whether or not this service is branching.
   * 
   * @param branching true to cause {@link #isBranching()} to return true; default is false.
   * @see com.adaptris.core.Service#isBranching()
   */
  public void setBranchingEnabled(Boolean branching) {
    this.branchingEnabled = branching;
  }

  /**
   * @return the builder
   */
  public ContainerBuilder getBuilder() {
    return builder;
  }

  /**
   * @param builder the builder to set
   */
  public void setBuilder(ContainerBuilder builder) {
    this.builder = builder;
  }

  /**
   * @return the initScript
   */
  public ScriptWrapper getInitScript() {
    return initScript;
  }

  /**
   * Set the script to be executed upon {@link Service#init()}.
   * 
   * @param s the initScript to set
   */
  public void setInitScript(ScriptWrapper s) {
    this.initScript = s;
  }

  /**
   * @return the startScript
   */
  public ScriptWrapper getStartScript() {
    return startScript;
  }

  /**
   * Set the script to be executed upon {@link Service#start()}.
   * 
   * @param s the startScript to set
   */
  public void setStartScript(ScriptWrapper s) {
    this.startScript = s;
  }

  /**
   * @return the stopScript
   */
  public ScriptWrapper getStopScript() {
    return stopScript;
  }

  /**
   * Set the script to be executed upon {@link Service#stop()}.
   * 
   * @param s the stopScript to set
   */
  public void setStopScript(ScriptWrapper s) {
    this.stopScript = s;
  }

  /**
   * 
   * @return the closeScript
   */
  public ScriptWrapper getCloseScript() {
    return closeScript;
  }

  /**
   * Set the script to be executed upon {@link Service#close()}.
   * 
   * @param s the closeScript to set
   */
  public void setCloseScript(ScriptWrapper s) {
    this.closeScript = s;
  }


  /**
   * @return the serviceScript
   */
  public ScriptWrapper getServiceScript() {
    return serviceScript;
  }

  /**
   * @param s the serviceScript to execute as part of {@link #doService(AdaptrisMessage)}
   */
  public void setServiceScript(ScriptWrapper s) {
    this.serviceScript = s;
  }

  private void executeQuietly(ScriptWrapper s) {
    try {
      ScriptingContainer container = getBuilder().build();
      execute(container, s);
    } catch (Exception e) {
      log.debug("Caught exception during script execution of [{}]", s, e);
    }
  }

  private void execute(ScriptingContainer container, ScriptWrapper s) throws CoreException {
    try {
      if (s != null) {
        container.put("log", log);
        container.runScriptlet(s.getPathType(), s.getLocation());
      }      
    } catch (Throwable e) {
      throw ExceptionHelper.wrapCoreException(e);
    }
  }

  private void destroy() {
    try {
      getBuilder().terminate(getBuilder().build());
    } catch (Exception e) {
      
    }
  }
}
