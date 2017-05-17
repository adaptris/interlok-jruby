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

import org.hibernate.validator.constraints.NotBlank;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.DynamicPollingTemplate;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Execute a jruby script using {@code JRuby Embed Core} Scripting container.
 * 
 * <p>
 * Rather than making use of the JSR223 bindings where things need to be controlled directly via system properties; this makes use
 * of {@code ScriptingContainer} to execute your jruby scripts which allows more fine-grained control over the behaviour.
 * </p>
 * <p>
 * The script is executed and the AdaptrisMessage that is due to be processed is bound as a local variable {@code message} and an
 * instance of org.slf4j.Logger is also bound as {@code log}. These can be used as a standard variable within the script.
 * </p>
 * 
 * @author lchan
 * @config jruby-scripting-service
 * 
 */
@XStreamAlias("jruby-scripting-service")
@AdapterComponent
@ComponentProfile(summary = "Execute a JRuby script stored on the filesystem", tag = "service,scripting", branchSelector = true)
@DisplayOrder(order = {"scriptFilename", "branchingEnabled"})
public class JRubyScriptingContainer extends ServiceImp implements DynamicPollingTemplate.TemplateProvider {

  private transient Logger log = LoggerFactory.getLogger(this.getClass().getName());

  @InputFieldDefault(value = "false")
  private Boolean branchingEnabled;
  @NotNull
  @Valid
  @AutoPopulated
  private ContainerBuilder builder;

  @NotBlank
  private String scriptFilename;
  public JRubyScriptingContainer() {
    super();
    setBuilder(new DefaultBuilder());
  }

  @Override
  public final void doService(AdaptrisMessage msg) throws ServiceException {
    ScriptingContainer container = getBuilder().build();
    container.put("message", msg);
    container.put("log", log);
    container.runScriptlet(PathType.ABSOLUTE, getScriptFilename());
  }

  @Override
  protected void initService() throws CoreException {
    getBuilder().init();
  }

  @Override
  protected void closeService() {
    getBuilder().close();
  }


  @Override
  public void start() throws CoreException {
    getBuilder().start();
    super.start();
  }

  @Override
  public void stop() {
    super.stop();
    getBuilder().stop();
  }

  @Override
  public void prepare() throws CoreException {
  }


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



  public String getScriptFilename() {
    return scriptFilename;
  }

  /**
   * Set the contents of the script.
   *
   * @param s the script
   */
  public void setScriptFilename(String s) {
    this.scriptFilename = s;
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

}
