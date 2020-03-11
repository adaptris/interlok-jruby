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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.List;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.junit.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceCase;
import com.adaptris.core.util.LifecycleHelper;

public class JRubyScriptingContainerTest extends ServiceCase {

  public static final String KEY_TEST_SCRIPT = "jruby.script";
  public static final String KEY_LIFECYCLE_SCRIPT = "jruby.lifecycle.script";
  public static final String KEY_JRUBY_HOME = "jruby.dir";

  public JRubyScriptingContainerTest() {
    super();
  }

  @Test
  public void testIsBranching() throws Exception {

    JRubyScriptingContainer service = new JRubyScriptingContainer();
    assertFalse(service.isBranching());
    service.setBranchingEnabled(true);
    assertTrue(service.isBranching());
    service.setBranchingEnabled(null);
    assertFalse(service.isBranching());
  }

  @Test
  public void testScript_DefaultBuilder() throws Exception {
    JRubyScriptingContainer service = new JRubyScriptingContainer();
    service.setServiceScript(new ScriptWrapper(PathType.ABSOLUTE, PROPERTIES.getProperty(KEY_TEST_SCRIPT)));
    service.setBuilder(new DefaultBuilder().withJrubyHome(PROPERTIES.getProperty(KEY_JRUBY_HOME)));
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    execute(service, msg);
    assertEquals("[\"bar\"]", msg.getContent());
  }

  @Test
  public void testScript_WithLifecycleScripts() throws Exception {
    JRubyScriptingContainer service = new JRubyScriptingContainer();

    service.setInitScript(new ScriptWrapper(PROPERTIES.getProperty(KEY_LIFECYCLE_SCRIPT)));
    service.setStartScript(new ScriptWrapper(PROPERTIES.getProperty(KEY_LIFECYCLE_SCRIPT)));
    service.setStopScript(new ScriptWrapper(PROPERTIES.getProperty(KEY_LIFECYCLE_SCRIPT)));
    service.setCloseScript(new ScriptWrapper(PROPERTIES.getProperty(KEY_LIFECYCLE_SCRIPT)));
    service.setServiceScript(new ScriptWrapper(PROPERTIES.getProperty(KEY_TEST_SCRIPT)));
    service.setBuilder(new DefaultBuilder().withJrubyHome(PROPERTIES.getProperty(KEY_JRUBY_HOME)));
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    execute(service, msg);
    assertEquals("[\"bar\"]", msg.getContent());
  }

  @Test
  public void testStart() throws Exception {
    JRubyScriptingContainer service = new JRubyScriptingContainer();
    service.setBuilder(new BrokenBuilder(BrokenBuilder.WhenToBreak.FIRST_BUILD_SUCCESSFUL));
    try {
      LifecycleHelper.initAndStart(service);
      fail();
    }
    catch (CoreException expected) {
      LifecycleHelper.stopAndClose(service);
    }
  }

  @Test
  public void testTerminate() throws Exception {
    JRubyScriptingContainer service = new JRubyScriptingContainer();
    service.setBuilder(new BrokenBuilder(BrokenBuilder.WhenToBreak.TERMINATE));
    LifecycleHelper.initAndStart(service);
    LifecycleHelper.stopAndClose(service);
    service.setBuilder(new DefaultBuilder());
    LifecycleHelper.initAndStart(service);
    LifecycleHelper.stopAndClose(service);
  }

  private JRubyScriptingContainer createService(ContainerBuilder b) {
    JRubyScriptingContainer service = new JRubyScriptingContainer();
    service.setServiceScript(new ScriptWrapper("/path/to/my.rb"));
    service.setBuilder(b);
    return service;
  }

  @Override
  protected List retrieveObjectsForSampleConfig() {
    return Arrays.asList(new JRubyScriptingContainer[] {createService(new DefaultBuilder().withJrubyHome("/path/to/jruby")),
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
  
  private static class BrokenBuilder extends DefaultBuilder {

    public static enum WhenToBreak {
      BUILD, TERMINATE, BOTH, FIRST_BUILD_SUCCESSFUL
    };

    private transient WhenToBreak whenToBreak;

    private transient int buildCount = 0;

    public BrokenBuilder(WhenToBreak b) {
      whenToBreak = b;
    }

    @Override
    public ScriptingContainer build() throws CoreException {
      if (whenToBreak == WhenToBreak.BUILD || whenToBreak == WhenToBreak.BOTH) {
        throw new CoreException("I'm Broken");
      }
      if (buildCount >= 1 && whenToBreak == WhenToBreak.FIRST_BUILD_SUCCESSFUL) {
        throw new CoreException("I'm Broken");
      }
      buildCount++;
      return super.build();
    }

    @Override
    public void terminate(ScriptingContainer c) {
      if (whenToBreak == WhenToBreak.TERMINATE || whenToBreak == WhenToBreak.BOTH) {
        throw new RuntimeException("I'm Broken");
      }
      super.terminate(c);
    }
    
  }

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
}
