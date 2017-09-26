package com.scarlatti.ork

/**
 * Created by pc on 9/25/2017.
 */
class IsolatedClassPathAction {

    def task

    void run() {}

    void run(Object closure) {
        closure.setDelegate(task)
        closure.run()
    }

    Object getTask() {
        return task
    }

    void setTask(Object task) {
        this.task = task
    }

    static IsolatedClassPathAction basic() {
        return new IsolatedClassPathAction() {}
    }
}