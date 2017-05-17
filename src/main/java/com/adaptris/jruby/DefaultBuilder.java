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

import static org.apache.commons.lang.StringUtils.isBlank;

import org.jruby.embed.ScriptingContainer;

import com.adaptris.core.CoreException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Build a {@code ScriptingContainer} that is configured with a {@code jruby.home}.
 * 
 * @author lchan
 * @config jruby-default-builder
 */
@XStreamAlias("jruby-default-builder")
public class DefaultBuilder extends ContainerBuilderImpl {

  private String rubyHome;

  public DefaultBuilder() {

  }

  public DefaultBuilder withRubyHome(String s) {
    setRubyHome(s);
    return this;
  }

  /**
   * @return the jrubyHomeDir
   */
  public String getRubyHome() {
    return rubyHome;
  }

  /**
   * @param d the jrubyHomeDir to set
   */
  public void setRubyHome(String d) {
    this.rubyHome = d;
  }

  protected ScriptingContainer configure(ScriptingContainer container) throws CoreException {
    String home = getRubyHome();
    if (!isBlank(home)) {
      container.setHomeDirectory(home);
    }
    return container;
  }
}
