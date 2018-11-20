package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
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

        BindInitHandle.initHandle(project)

        inputs.each { TransformInput input ->

            input.jarInputs.each { JarInput jarInput ->
                BindInitHandle.insertClassPath(jarInput.file.absolutePath)
                def jarName = jarInput.name
                println("jar=" + jarInput.file.getAbsolutePath())
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }

            BindInitHandle.inject(input.directoryInputs)
            input.directoryInputs.each { DirectoryInput directoryInput ->
                def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

        }

        System.out.println("-------------------------transform bindinit end-----------------------------")

    }

}