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

import static com.adaptris.jruby.JRubyScriptingContainerTest.KEY_JRUBY_HOME;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.adaptris.interlok.junit.scaffolding.BaseCase;

public class TestAdvancedBuilder extends BaseCase {

  private static final String PATH_TO_GEMS = "lib/ruby/gems/shared/gems";
  private static final String PATH_TO_SPECS = "lib/ruby/gems/shared/specifications";

  @Test
  public void testBuild() throws Exception {
    AdvancedBuilder b = new AdvancedBuilder();
    assertNotNull(b.build());
  }

  @Test
  public void testBuild_WithNullGemDir() throws Exception {
    AdvancedBuilder b = new AdvancedBuilder().withGemdirs("/path/that/does/not/exist", (String) null).withAddSubdirs(true);
    assertNotNull(b.build());
  }

  @Test
  public void testBuild_WithGemdirs() throws Exception {
    String jrubyhome = PROPERTIES.getProperty(KEY_JRUBY_HOME);
    String gemspecdir = String.format("%s/%s", jrubyhome, PATH_TO_SPECS);
    String gemdir = String.format("%s/%s", jrubyhome, PATH_TO_GEMS);

    AdvancedBuilder b = new AdvancedBuilder().withGemdirs(gemspecdir, gemdir).withAddSubdirs(true);
    assertNotNull(b.build());
  }

  @Test
  public void testBuild_WithLoadPath() throws Exception {
    String jrubyhome = PROPERTIES.getProperty(KEY_JRUBY_HOME);
    String d1 = String.format("%s/%s", jrubyhome, PATH_TO_SPECS);
    String d2 = String.format("%s/%s", jrubyhome, PATH_TO_GEMS);

    AdvancedBuilder b = new AdvancedBuilder().withLoadPaths(d1, d2);
    assertNotNull(b.build());
  }

}
