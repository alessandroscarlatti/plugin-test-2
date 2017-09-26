package com.scarlatti.ork

import org.gradle.api.DefaultTask
import org.sonar.classloader.ClassloaderBuilder
import org.sonar.classloader.Mask

/**
 * Created by pc on 9/25/2017.
 */
class PluginUtils {

    /**
     * ClassLoader for this Plugin.
     */
    static enum IsolatedClassPathClassLoader {
        INSTANCE

        private ClassLoader selfFirstClassLoader
        private ClassLoader childClassLoader

        Object getTaskClassInstance(File pluginJar, Class clazz) {

            if (childClassLoader == null) {
                URL url = pluginJar.toURI().toURL()
                URL[] urls = [url]
                childClassLoader = new URLClassLoader(urls)

                println "childClassLoader urls: ${childClassLoader.URLs}"
            }

            return childClassLoader.loadClass(clazz.name).newInstance()
        }

        Object getTaskClassInstance(File pluginJar, ClassLoader parent, Class clazz) {

            if (childClassLoader == null) {
//                URL url = pluginJar.toURI().toURL()
//                URL[] urls = [url]
//                childClassLoader = new URLClassLoader(urls)
//
//                println "childClassLoader urls: ${childClassLoader.URLs}"
            }

            if (selfFirstClassLoader == null) {

                Map<String, ClassLoader> classLoaderMap = new ClassloaderBuilder()
                    .newClassloader("child")
                    .addURL("child", pluginJar.toURI().toURL())
                    .setParent("child", parent, new Mask())
                    .setLoadingOrder("child", ClassloaderBuilder.LoadingOrder.SELF_FIRST)
                    .build()

                selfFirstClassLoader = classLoaderMap.get("child")
            }

            return selfFirstClassLoader.loadClass(clazz.name).newInstance()
        }
    }

    static void performAction(Class clazz, DefaultTask task) {
        // call the class loader
        // load the class
        // call the run method

        // TODO this can be elsewhere
        Set<File> files = task.project.buildscript.configurations.classpath.files { it.name.contains('ork-plugin')}

        if (files.size() == 0 || files.size() > 1) {
            throw new RuntimeException("wrong files size! files: ${files}")
        }

        println "files: ${files}"

        println "taskClass: $clazz.canonicalName"


        println 'performing task.'

        Object isolatedAction = IsolatedClassPathClassLoader.INSTANCE.getTaskClassInstance(files[0], task.class.classLoader, clazz)

        isolatedAction.invokeMethod('setTask', task)
        isolatedAction.invokeMethod('run', null)

        IsolatedClassPathClassLoader.INSTANCE.childClassLoader = null
        IsolatedClassPathClassLoader.INSTANCE.selfFirstClassLoader = null
    }

    static void performAction(DefaultTask task, Object closure) {
        // call the class loader
        // load the class
        // call the run method

        // TODO this can be elsewhere
        Set<File> files = task.project.buildscript.configurations.classpath.files { it.name.contains('ork-plugin')}

        if (files.size() == 0 || files.size() > 1) {
            throw new RuntimeException("wrong files size! files: ${files}")
        }

        println "files: ${files}"

        println 'performing task.'

        Object isolatedAction = IsolatedClassPathClassLoader.INSTANCE.getTaskClassInstance(files[0], task.class.classLoader, IsolatedClassPathAction)

        isolatedAction.invokeMethod('setTask', task)
        isolatedAction.invokeMethod('run', closure)

        IsolatedClassPathClassLoader.INSTANCE.childClassLoader = null
        IsolatedClassPathClassLoader.INSTANCE.selfFirstClassLoader = null
    }
}
