package com.scarlatti.ork

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by pc on 9/16/2017.
 */
class OrkPlugin implements Plugin<Project> {
    void apply(Project project) {

        project.tasks.create(name: 'ork', group: 'ork', type: SimpleOrkTask)
        project.tasks.create(name: 'special', group:  'ork', type: SpecialTask)

        println 'this is version H'
    }
}
