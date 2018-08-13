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

import org.jruby.embed.ScriptingContainer;

import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.CoreException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Build a {@code ScriptingContainer} that is configured with a {@code jruby.home}.
 * 
 * @author lchan
 * @config jruby-default-builder
 */
@XStreamAlias("jruby-default-builder")
@DisplayOrder(order = {"jrubyHome", "loadPaths", "contextScope", "variableBehaviour", "compileMode"})
public class DefaultBuilder extends ContainerBuilderImpl {

  protected ScriptingContainer configure(ScriptingContainer container) throws CoreException {
    return container;
  }
}
