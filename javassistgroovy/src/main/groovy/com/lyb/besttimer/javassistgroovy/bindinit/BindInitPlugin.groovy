package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class BindInitPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("---------------------------------- plugin bindInit begin -------------------------------")
        System.out.println("test plugin")

        def android = project.extensions.getByType(AppExtension)

        def classTransform = new BindInitTransform(project)
        android.registerTransform(classTransform)

        System.out.println("---------------------------------- plugin bindInit end -------------------------------")
    }

}