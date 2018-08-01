package com.lyb.besttimer.javassistgroovy

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("----------------------------------begin-------------------------------")
        System.out.println("test plugin")

        def android = project.extensions.getByType(AppExtension)

        def classTransform = new TestTransform(project)
        android.registerTransform(classTransform)

        System.out.println("---------------------------------- end -------------------------------")
    }

}