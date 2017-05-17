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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.adaptris.core.BaseCase;

public class TestDefaultBuilder {

  @Test
  public void testBuild() throws Exception {
    DefaultBuilder b = new DefaultBuilder();
    try {
      BaseCase.start(b);
      assertNotNull(b.build());
    } finally {
      BaseCase.stop(b);
    }
  }

  @Test
  public void testBuild_WithHome() throws Exception {
    DefaultBuilder b = new DefaultBuilder().withRubyHome("/tmp");
    try {
      BaseCase.start(b);
      assertNotNull(b.build());
    } finally {
      BaseCase.stop(b);
    }
  }
}
