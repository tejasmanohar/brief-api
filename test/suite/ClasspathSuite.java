package suite;

import org.junit.Test;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import play.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class ClasspathSuite extends Suite {

    private static final String CLASSPATH = System.getProperty("java.class.path");
    private static final String CLASS_EXT = ".class";

    public ClasspathSuite(Class<?> setupClass) throws InitializationError {
        super(setupClass, getTestClasses());
    }

    private static Class<?>[] getTestClasses() throws InitializationError {
        List<Class<?>> testClasses = new LinkedList<Class<?>>();

        for (String each : CLASSPATH.split(File.pathSeparator)) {
            File root = new File(each);

            // We're really only interested in classpath directories as we'd
            // like to run tests in this project. If other tests are included
            // on the classpath in libraries we'd like not to know of them.
            if (root.isDirectory()) {
                gatherTestClasses(root, root, testClasses);
            }
        }
        return testClasses.toArray(new Class<?>[testClasses.size()]);
    }

    private static void gatherTestClasses(File root, File cur, List<Class<?>> classes) throws InitializationError {
        for (File each : cur.listFiles()) {
            if (each.isDirectory()) {
                gatherTestClasses(root, each, classes);
            } else if (each.getName().endsWith(CLASS_EXT)) {
                try {
                    Class<?> clazz = Class.forName(getClassName(each.getPath(), root.getPath()));
                    if (isTestClass(clazz)) {
                        Logger.info("Test class: " + clazz.getSimpleName());
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    throw new InitializationError(e);
                }
            }
        }
    }

    private static String getClassName(String fileName, String classPathRoot) {
        String className = fileName.substring(classPathRoot.length());
        if (className.charAt(0) == File.separatorChar) {
            className = className.substring(1);
        }
        return className.substring(0, className.length() - CLASS_EXT.length()).replace(File.separatorChar, '.');
    }

    private static boolean isTestClass(Class<?> clazz) {
        if (isAbstractClass(clazz)) {
            return false;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (isTest(method)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAbstractClass(Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    private static boolean isTest(Method method) {
        return method.getAnnotation(Test.class) != null;
    }
}

