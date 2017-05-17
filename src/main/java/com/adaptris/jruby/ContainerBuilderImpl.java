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

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;

public abstract class ContainerBuilderImpl implements ContainerBuilder {

  private transient ScriptingContainer container;
  @Valid
  @NotNull
  @AutoPopulated
  @InputFieldDefault("TRANSIENT")
  @AdvancedConfig
  private LocalContextScope contextScope;
  @Valid
  @NotNull
  @AutoPopulated
  @InputFieldDefault(value = "THREADSAFE")
  @AdvancedConfig
  private LocalVariableBehavior variableBehaviour;

  public ContainerBuilderImpl() {
    setContextScope(LocalContextScope.THREADSAFE);
    setVariableBehaviour(LocalVariableBehavior.TRANSIENT);
  }


  @Override
  public ScriptingContainer build() throws ServiceException {
    return container;
  }

  @Override
  public void init() throws CoreException {
    container = configure(new ScriptingContainer(getContextScope(), getVariableBehaviour()));
  }

  @Override
  public void start() throws CoreException {}

  @Override
  public void stop() {}

  @Override
  public void close() {}

  protected abstract ScriptingContainer configure(ScriptingContainer c) throws CoreException;

  /**
   * @return the contextScope
   */
  public LocalContextScope getContextScope() {
    return contextScope;
  }

  /**
   * Set the context scope
   * 
   * @param c the contextScope to set; default is {@code LocalContextScope.THREADSAFE}
   */
  public void setContextScope(LocalContextScope c) {
    this.contextScope = c;
  }

  /**
   * @return the variableBehaviour
   */
  public LocalVariableBehavior getVariableBehaviour() {
    return variableBehaviour;
  }

  /**
   * Set the variable behaviour
   * 
   * @param b the variableBehaviour to set; default is {@code LocalVariableBehavior.TRANSIENT}
   */
  public void setVariableBehaviour(LocalVariableBehavior b) {
    this.variableBehaviour = b;
  }
}
