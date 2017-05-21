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

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.jruby.RubyInstanceConfig.CompileMode;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.CoreException;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public abstract class ContainerBuilderImpl implements ContainerBuilder {

  private transient ScriptingContainer container;
  @Valid
  @NotNull
  @AutoPopulated
  @InputFieldDefault("THREADSAFE")
  @AdvancedConfig
  private LocalContextScope contextScope;
  @Valid
  @NotNull
  @AutoPopulated
  @InputFieldDefault(value = "TRANSIENT")
  @AdvancedConfig
  private LocalVariableBehavior variableBehaviour;
  @Valid
  @NotNull
  @AutoPopulated
  @InputFieldDefault(value = "JIT")
  @AdvancedConfig
  private CompileMode compileMode;
  @XStreamImplicit(itemFieldName = "load-path")
  @NotNull
  @AutoPopulated
  private List<String> loadPaths;


  public ContainerBuilderImpl() {
    setContextScope(LocalContextScope.THREADSAFE);
    setVariableBehaviour(LocalVariableBehavior.TRANSIENT);
    setLoadPaths(new ArrayList<String>());
    setCompileMode(CompileMode.JIT);
  }


  @Override
  public ScriptingContainer build() throws CoreException {
    if (container == null) {
      container = new ScriptingContainer(getContextScope(), getVariableBehaviour());
      container.setCompileMode(getCompileMode());
      container.setLoadPaths(getLoadPaths());
      container = configure(container);
    }
    return container;
  }

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

  /**
   * @return the loadPaths
   */
  public List<String> getLoadPaths() {
    return loadPaths;
  }

  /**
   * Set any additional directories that need to be added via {@code ScriptingContainer.setLoadPaths()}.
   * 
   * @param paths the loadPaths to set
   */
  public void setLoadPaths(List<String> paths) {
    this.loadPaths = paths;
  }

  public ContainerBuilderImpl withLoadPaths(String... strings) {
    for (String s : strings) {
      getLoadPaths().add(s);
    }
    return this;
  }


  /**
   * @return the compileMode
   */
  public CompileMode getCompileMode() {
    return compileMode;
  }


  /**
   * @param m the compileMode to set; default is {@code JIT}
   */
  public void setCompileMode(CompileMode m) {
    this.compileMode = m;
  }
}
