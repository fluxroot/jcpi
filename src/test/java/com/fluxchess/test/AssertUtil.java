/*
 * Copyright 2007-2015 the original author or authors.
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
package com.fluxchess.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class AssertUtil {

  public static void assertUtilityClassWellDefined(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    assertTrue(String.format("%s must be final", clazz), Modifier.isFinal(clazz.getModifiers()));

    assertEquals("There must be only one constructor", 1, clazz.getDeclaredConstructors().length);

    Constructor<?> constructor = clazz.getDeclaredConstructor();
    assertTrue("Constructor must be private", Modifier.isPrivate(constructor.getModifiers()));

    for (Method method : clazz.getMethods()) {
      if (method.getDeclaringClass().equals(clazz)) {
        assertTrue(String.format("%s is not static", method), Modifier.isStatic(method.getModifiers()));
      }
    }

    constructor.setAccessible(true);
    constructor.newInstance();
    constructor.setAccessible(false);
  }

}
