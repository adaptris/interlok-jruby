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

import java.util.Arrays;
import java.util.List;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.ServiceCase;

public class JRubyScriptingContainerTest extends ServiceCase {

  public static final String KEY_TEST_SCRIPT = "jruby.script";
  public static final String KEY_JRUBY_HOME = "jruby.dir";

  public JRubyScriptingContainerTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {}

  public void tearDown() throws Exception {}

  public void testScript_DefaultBuilder() throws Exception {
    JRubyScriptingContainer service = new JRubyScriptingContainer();
    service.setScriptFilename(PROPERTIES.getProperty(KEY_TEST_SCRIPT));
    service.setBuilder(new DefaultBuilder().withRubyHome(PROPERTIES.getProperty(KEY_JRUBY_HOME)));
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    execute(service, msg);
    assertEquals("[\"bar\"]", msg.getContent());
  }

  private JRubyScriptingContainer createService(ContainerBuilder b) {
    JRubyScriptingContainer service = new JRubyScriptingContainer();
    service.setScriptFilename("/path/to/my.rb");
    service.setBuilder(b);
    return service;
  }

  @Override
  protected List retrieveObjectsForSampleConfig() {
    return Arrays.asList(new JRubyScriptingContainer[] {createService(new DefaultBuilder().withRubyHome("/path/to/jruby")),
        createService(new AdvancedBuilder().withGemdirs("/path/to/gems", "/path/to/specifications")
            .withLoadPaths("/path/to/loaddir1", "/path/to/loaddir2"))});
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    return null;
  }


  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-" + ((JRubyScriptingContainer) object).getBuilder().getClass().getSimpleName();
  }
}
