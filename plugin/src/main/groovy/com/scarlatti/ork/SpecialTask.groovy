package com.scarlatti.ork

import com.mashape.unirest.http.Unirest
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.HttpClients
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by pc on 9/25/2017.
 */
class SpecialTask extends DefaultTask {

    Penguin penguin

    @TaskAction
    void run() {
        println 'running special task...'

        penguin = new Penguin()
        penguin.name = "annsadfklj"

        PluginUtils.performAction(this) {
            println 'performing Action!'

            println "running with project: ${project.name}"

            logger.lifecycle "penguin is: ${penguin.name}"

            Unirest.httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build()

            println """
                response: ${
                    Unirest.get('https://www.google.com').asString().body
                }
            """

            Unirest.shutdown()
        }
    }

//    static class Action extends IsolatedClassPathAction {
//
//        @Override
//        void run() {
//            println 'performing Action!'
//
//            println "running with project: ${task.project.name}"
//
//            task.logger.lifecycle "penguin is: ${task.penguin.name}"
//
//            Unirest.httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build()
//
//            println """
//            response: ${
//                Unirest.get('https://www.google.com').asString().body
//            }
//
//        """
//
//            Unirest.shutdown()
//        }
//    }
}
