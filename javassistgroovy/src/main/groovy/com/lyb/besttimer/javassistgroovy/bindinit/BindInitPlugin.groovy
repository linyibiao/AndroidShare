package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class BindInitPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("---------------------------------- plugin bindInit init begin -------------------------------")

        try {

            AppExtension android = project.extensions.getByType(AppExtension)
////        def android = project.extensions.getByType(LibraryExtension)

            def classTransform = new BindInitTransform(project)
            android.registerTransform(classTransform)

//            project.afterEvaluate {
//                BindInitHandle.pluginBean=project.extensions.getByName("plugin_bindinit")
//            }

//            project.android.registerTransform(new BindInitTransform(project))

        } catch (Exception e) {
            e.printStackTrace()
        }

//        project.android.registerTransform(new BindInitTransform(project))

        System.out.println("---------------------------------- plugin bindInit init end -------------------------------")
    }

}