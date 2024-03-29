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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.jruby.RubyInstanceConfig.CompileMode;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.junit.jupiter.api.Test;

public class TestDefaultBuilder {

  @Test
  public void testBuild() throws Exception {
    DefaultBuilder b = new DefaultBuilder().withCompileMode(CompileMode.JIT).withContextScope(LocalContextScope.THREADSAFE)
        .withVariableBehaviour(LocalVariableBehavior.TRANSIENT);
    assertNotNull(b.build());
  }

  @Test
  public void testBuild_WithHome() throws Exception {
    DefaultBuilder b = new DefaultBuilder().withJrubyHome("/tmp");
    assertNotNull(b.build());
  }

  @Test
  public void testTerminate_IsEqual() throws Exception {
    DefaultBuilder b = new DefaultBuilder();
    ScriptingContainer c = b.build();
    b.terminate(c);
    b.terminate(null);
    assertNotSame(c, b.build());
  }

  @Test
  public void testTerminate_NotEqual() throws Exception {
    DefaultBuilder b = new DefaultBuilder();
    ScriptingContainer c = b.build();
    b.terminate(new ScriptingContainer());
    assertEquals(c, b.build());
  }

}
