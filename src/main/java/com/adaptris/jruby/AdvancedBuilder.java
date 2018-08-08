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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.jruby.embed.ScriptingContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
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
@DisplayOrder(order = {"jrubyhome", "gemdirs", "loadPaths", "contextScope", "variableBehaviour", "compileMode"})
public class AdvancedBuilder extends ContainerBuilderImpl {
  private static transient Logger log = LoggerFactory.getLogger(AdvancedBuilder.class);

  @XStreamImplicit(itemFieldName = "gemdir")
  private List<String> gemdirs;

  @InputFieldDefault(value = "true")
  private Boolean addSubdirs;

  public AdvancedBuilder() {
    super();
    setGemdirs(new ArrayList<String>());
  }

  protected ScriptingContainer configure(ScriptingContainer container) throws CoreException {
    try {
      container.addClassLoader(new URLClassLoader(toURL(getGemdirs(), addSubdirs())));
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
   * Under the covers this effectively does {@code ScriptingContainer.addClassLoader()} with a {@link URLClassLoader} built from the
   * list of directories specified here (the actual directories might be affected by {@link #setAddSubdirs(Boolean)}).
   * </p>
   * 
   * @param gemdirs the list of gemdirs to set
   */
  public void setGemdirs(List<String> gemdirs) {
    this.gemdirs = gemdirs;
  }

  private static final URL[] toURL(List<String> gemdirs, boolean addSubdirs) throws MalformedURLException {
    List<URL> urls = new ArrayList<URL>();
    for (String gemdir : gemdirs) {
      if (gemdir != null) {
        File directory = new File(gemdir);
        urls.add(directory.toURI().toURL());
        if (addSubdirs) {
          urls.addAll(getChildren(directory));
        }
      }
    }
    log.trace("Using gemdirs: [{}]", urls);
    return urls.toArray(new URL[0]);
  }

  public Boolean getAddSubdirs() {
    return addSubdirs;
  }

  /**
   * Set whether or not sub directories will be added.
   * <p>
   * This is really just a convenience so that if you specify a gemdir of {@code /home/vagrant/.rvm/gems/jruby-9.1.17.0/gems} then
   * it will add the contents of that directory in addition to the directory itself. This is to avoid you having to manually specify
   * all the possible gems that are required (e.g. a list containing
   * {@code /home/vagrant/.rvm/gems/jruby-9.1.17.0/gems/jsonpath-0.9.3, /home/vagrant/.rvm/gems/jruby-9.1.17.0/gems/multi-json-1.13.1}
   * etc.
   * </p>
   * 
   * @param b true to additionally include the first level of sub-directories of any specified directory, default is true.
   */
  public void setAddSubdirs(Boolean b) {
    this.addSubdirs = b;
  }

  public AdvancedBuilder withAddSubdirs(Boolean b) {
    setAddSubdirs(b);
    return this;
  }

  protected boolean addSubdirs() {
    return BooleanUtils.toBooleanDefaultIfNull(getAddSubdirs(), true);
  }

  private static List<URL> getChildren(File dir) {
    List<URL> result = new ArrayList<>();
    try {
      result =  Arrays.asList(dir.listFiles((pathname) -> pathname.isDirectory())).stream().map(AdvancedBuilder::toURL)
          .collect(Collectors.toList());
    } catch (NullPointerException e) {
      // listFiles returns null on IO exceptions etc.
    }
    return result;
  }

  private static URL toURL(File f) {
    try {
      return f.toURI().toURL();
    } catch (MalformedURLException e1) {
      throw new RuntimeException(e1);
    }
  }

}
