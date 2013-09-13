/*
 * Copyright 2013 Decebal Suiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.gogo.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Decebal Suiu
 */
public class ReflectionUtils {

    /**
     * Invokes the application in this jar file given the name of the main class
     * and an array of arguments. The class must define a static method "main"
     * which takes an array of String arguemtns and is of return type "void".
     *
     * @param className
     *                   the name of the main class
     * @param args
     *                   the arguments for the application
     * @exception ClassNotFoundException
     *                        if the specified class could not be found
     * @exception NoSuchMethodException
     *                        if the specified class does not contain a "main" method
     * @exception InvocationTargetException
     *                        if the application raised an exception
     */
    public static void invokeMain(String className, String[] args, ClassLoader classLoader) throws ClassNotFoundException,
    		NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = classLoader.loadClass(className);
        Method method = clazz.getMethod("main", new Class[] { args.getClass() });
        method.setAccessible(true);
        int mods = method.getModifiers();
        if (method.getReturnType() != void.class || !Modifier.isStatic(mods)
                || !Modifier.isPublic(mods)) {
            throw new NoSuchMethodException("main");
        }
        try {
            method.invoke(null, new Object[] { args });
        } catch (IllegalAccessException e) {
            // this should not happen, as we have disabled access checks
        }
    }

}
