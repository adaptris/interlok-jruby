/*
 * Copyright 2017 Adaptris Ltd.
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
package com.adaptris.jruby;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;
import org.jruby.embed.PathType;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.InputFieldDefault;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Wraps a jruby script location.
 * 
 * @author lchan
 *
 */
@XStreamAlias("jruby-script-wrapper")
public class ScriptWrapper {

  @NotBlank
  private String location;
  @NotNull
  @AutoPopulated
  @InputFieldDefault(value = "ABSOLUTE")
  private PathType pathType;

  public ScriptWrapper() {
    setPathType(PathType.ABSOLUTE);
  }

  public ScriptWrapper(String location) {
    this(PathType.ABSOLUTE, location);
  }

  public ScriptWrapper(PathType pathtype, String location) {
    this();
    setPathType(pathtype);
    setLocation(location);
  }

  /**
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * @return the pathType
   */
  public PathType getPathType() {
    return pathType;
  }

  /**
   * @param type the pathType to set, defaults to {@link PathType.ABSOLUTE}.
   */
  public void setPathType(PathType type) {
    this.pathType = type;
  }

  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("location", getLocation())
        .append("pathtype", getPathType()).toString();
  }

}
