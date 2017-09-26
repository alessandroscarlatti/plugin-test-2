package com.scarlatti.ork

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by pc on 9/16/2017.
 */
class SimpleOrkTask extends DefaultTask {

    String penguinName
    String message
    String when

    @TaskAction
    void simpleOrk() {
        buildProps()
        doOrk()
    }

    void doOrk() {
        println "$penguinName says $message $when"
    }

    void buildProps() {

        File defaultFile = project.file('orkProps/defaults.properties')
        File customFile = project.file('orkProps/ork.properties')

        Properties defaultProps = new Properties()
        Properties customProps = new Properties()

        try {
            defaultProps.load(new FileInputStream(defaultFile))
        } catch (FileNotFoundException e) {
            logger.warn "no default properties found at ${defaultFile.absolutePath}"
        }

        try {
            customProps.load(new FileInputStream(customFile))
        } catch (FileNotFoundException e) {
            logger.warn "no environment-specific properties found at ${customFile.absolutePath}"
        }



        for (String key : customProps.keySet()) {
            println "found key ${customProps.getProperty(key)}"
        }

        penguinName = customProps.penguinName ?: defaultProps.penguinName
        message = customProps.message ?: defaultProps.message
        when = customProps.when ?: defaultProps.when

        when = project.properties.WHEN ?: when
    }
}
