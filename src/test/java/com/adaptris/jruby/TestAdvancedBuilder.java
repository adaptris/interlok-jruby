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

import static com.adaptris.jruby.JRubyScriptingContainerTest.KEY_JRUBY_HOME;

import org.junit.Test;

import com.adaptris.core.BaseCase;

public class TestAdvancedBuilder extends BaseCase {

  private static final String PATH_TO_GEMS = "lib/ruby/gems/shared/gems";
  private static final String PATH_TO_SPECS = "lib/ruby/gems/shared/specifications";

  public TestAdvancedBuilder(String name) {
    super(name);
  }


  @Test
  public void testBuild() throws Exception {
    AdvancedBuilder b = new AdvancedBuilder();
    try {
      start(b);
      assertNotNull(b.build());
    } finally {
      stop(b);
    }
  }

  @Test
  public void testBuild_WithGemdirs() throws Exception {
    String jrubyhome = PROPERTIES.getProperty(KEY_JRUBY_HOME);
    String gemspecdir = String.format("%s/%s", jrubyhome,  PATH_TO_SPECS);
    String gemdir = String.format("%s/%s", jrubyhome, PATH_TO_GEMS);

    AdvancedBuilder b = new AdvancedBuilder().withGemdirs(gemspecdir, gemdir);
    try {
    BaseCase.start(b);
    assertNotNull(b.build());
    } finally {
      stop(b);
    }
  }

  @Test
  public void testBuild_WithLoadPath() throws Exception {
    String jrubyhome = PROPERTIES.getProperty(KEY_JRUBY_HOME);
    String d1 = String.format("%s/%s", jrubyhome, PATH_TO_SPECS);
    String d2 = String.format("%s/%s", jrubyhome, PATH_TO_GEMS);

    AdvancedBuilder b = new AdvancedBuilder().withLoadPaths(d1, d2);
    try {
      BaseCase.start(b);
      assertNotNull(b.build());
    } finally {
      stop(b);
    }
  }
}
