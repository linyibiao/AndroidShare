package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project

public class BindInitTransform extends Transform {

    private final Project project

    public BindInitTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "TransformBindInit"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider

        System.out.println("-------------------------transform bindinit begin-----------------------------")

        BindInitHandle.injectInput(project, outputProvider, inputs)

        System.out.println("-------------------------transform bindinit end-----------------------------")

    }

}