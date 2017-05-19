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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.jruby.embed.ScriptingContainer;

import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.ExceptionHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Build a {@code ScriptingContainer} that is configured with a list of directories from which to load gems / add to the load path.
 * 
 * @author lchan
 *
 */
@XStreamAlias("jruby-advanced-builder")
@DisplayOrder(order = {"gemdirs", "loadPaths", "contextScope", "variableBehaviour"})
public class AdvancedBuilder extends ContainerBuilderImpl {

  @XStreamImplicit(itemFieldName = "gemdir")
  private List<String> gemdirs;


  public AdvancedBuilder() {
    super();
    setGemdirs(new ArrayList<String>());
  }

  protected ScriptingContainer configure(ScriptingContainer container) throws CoreException {
    try {
      container.addClassLoader(new URLClassLoader(toURL(getGemdirs())));
    } catch (Exception e) {
      throw ExceptionHelper.wrapCoreException(e);
    }
    return container;
  }

  public AdvancedBuilder withGemdirs(String... strings) {
    for (String s : strings) {
      getGemdirs().add(s);
    }
    return this;
  }

  /**
   * @return the gemdirs
   */
  public List<String> getGemdirs() {
    return gemdirs;
  }

  /**
   * Set the directories from which to try and load gems
   * <p>
   * Under the covers this effectively does {@code ScriptingContainer.addClassLoader()} with a {@link URLClassLoader} built from
   * the list of directories specified here.
   * </p>
   * 
   * @param gemdirs the list of gemdirs to set
   */
  public void setGemdirs(List<String> gemdirs) {
    this.gemdirs = gemdirs;
  }

  private static final URL[] toURL(List<String> gemdirs) throws MalformedURLException {
    List<URL> urls = new ArrayList<URL>(gemdirs.size());
    for (String dir : gemdirs) {
      urls.add(new File(dir).toURI().toURL());
    }
    return urls.toArray(new URL[gemdirs.size()]);
  }



}
