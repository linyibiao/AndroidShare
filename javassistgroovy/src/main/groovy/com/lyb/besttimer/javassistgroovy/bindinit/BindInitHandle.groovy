package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.SdkConstants
import com.android.build.api.transform.*
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

public class BindInitHandle {

    private final static ClassPool pool = ClassPool.getDefault()

    public static void initHandle(Project project) {
        project.android.bootClasspath.each {
            pool.appendClassPath(it.absolutePath)
        }
    }

    public static void insertClassPath(String path) {
        pool.insertClassPath(path)
    }

    static class KeyValue {
        public CtClass key
        public String value

        KeyValue(CtClass key, String value) {
            this.key = key
            this.value = value
        }
    }

    static List<File> getAllFiles(File file) {
        if (file.isDirectory()) {
            List<File> files = new ArrayList<>();
            for (File childFile : file.listFiles()) {
                files.addAll(getAllFiles(childFile))
            }
            return files
        } else {
            return Collections.singletonList(file)
        }
    }

    static void injectInput(Project project, TransformOutputProvider outputProvider, Collection<TransformInput> inputs) {

        initHandle(project)

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                println("ZipEntry:" + jarInput.file.absolutePath)
                insertClassPath(jarInput.file.absolutePath)
            }
            input.directoryInputs.each { DirectoryInput directoryInput ->
                println("ClassPath:" + directoryInput.file.absolutePath)
                insertClassPath(directoryInput.file.absolutePath)
            }
        }

//        CtClass appClass = pool.getCtClass("android.app.Application")
        List<KeyValue> targetClassAndPathList = new ArrayList<>()//目标信息列表
        List<CtClass> initClassList = new ArrayList<>()//需要初始化列表

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                ZipInputStream inputStream = null
                try {
                    inputStream = new ZipInputStream(new FileInputStream(jarInput.file))

                    ZipEntry entry = inputStream.getNextEntry()
                    while (entry != null) {
                        String filePath = entry.getName()
                        println("ZipEntry:" + filePath)
                        if (filePath.endsWith(SdkConstants.DOT_CLASS)) {
                            final String classPath = filePath.replace("\\", ".").replace("/", ".").replace(SdkConstants.DOT_CLASS, "")
                            CtClass ctClass = pool.getCtClass(classPath)
                            CtClass[] interfaces = ctClass.getInterfaces()
                            if (pool.getCtClass("android.app.Application") == ctClass.getSuperclass()) {
                                println("target:" + filePath)
                                targetClassAndPathList.add(new KeyValue(ctClass, dirPath))
                            } else if (interfaces != null) {
                                if (interfaces.contains(pool.getCtClass("com.lyb.besttimer.annotation_bean.IAppInit"))) {
                                    println("toInit:" + filePath)
                                    initClassList.add(ctClass)
                                }
                            }
                        }
                        entry = inputStream.getNextEntry()
                    }

                } catch (Exception e) {
                    e.printStackTrace()
                } finally {
                    if (inputStream != null) {
                        inputStream.close()
                    }
                }
            }
            input.directoryInputs.each { DirectoryInput directoryInput ->
                String dirPath = directoryInput.file.absolutePath
                List<File> childFiles = getAllFiles(directoryInput.file)
                for (File file : childFiles) {
                    String filePath = file.absolutePath
                    println(filePath)
                    if (filePath.endsWith(SdkConstants.DOT_CLASS)) {
                        String debugStr = "debug"
                        String releaseStr = "release"
                        final String classPath
                        //截出正确的类路径
                        if (filePath.contains(debugStr)) {
                            classPath = filePath.substring(filePath.indexOf(debugStr) + debugStr.length() + 1).replace("\\", ".").replace("/", ".").replace(SdkConstants.DOT_CLASS, "")
                        } else {
                            classPath = filePath.substring(filePath.indexOf(releaseStr) + releaseStr.length() + 1).replace("\\", ".").replace("/", ".").replace(SdkConstants.DOT_CLASS, "")
                        }
                        CtClass ctClass = pool.getCtClass(classPath)
                        CtClass[] interfaces = ctClass.getInterfaces()
                        if (pool.getCtClass("android.app.Application") == ctClass.getSuperclass()) {
                            println("target:" + filePath)
                            targetClassAndPathList.add(new KeyValue(ctClass, dirPath))
                        } else if (interfaces != null) {
                            if (interfaces.contains(pool.getCtClass("com.lyb.besttimer.annotation_bean.IAppInit"))) {
                                println("toInit:" + filePath)
                                initClassList.add(ctClass)
                            }
                        }
                    }
                }
            }
        }

        //加一个bindInit方法到目标
        for (KeyValue targetClassAndPath : targetClassAndPathList) {
            CtClass targetClass = targetClassAndPath.key
            String targetPath = targetClassAndPath.value
            String methodStr = "void bindInit(){"
            if (targetClass.isFrozen()) {
                targetClass.defrost()
            }
            for (CtClass oneInitClass : initClassList) {
                if (oneInitClass.isFrozen()) {
                    oneInitClass.defrost()
                }
                methodStr += "new " + oneInitClass.getName() + "().init(this);"
            }
            methodStr += "}"
            CtMethod initMethod = CtMethod.make(methodStr, targetClass)
            targetClass.addMethod(initMethod)

            CtMethod createMethod = targetClass.getDeclaredMethod("onCreate")
            createMethod.insertAfter("""bindInit();""")

            targetClass.writeFile(targetPath)

        }

        for (KeyValue targetClassAndPath : targetClassAndPathList) {
            targetClassAndPath.key.detach()
        }
        for (CtClass ctClass : initClassList) {
            ctClass.detach()
        }

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                def jarName = jarInput.name
                println("jar=" + jarInput.file.getAbsolutePath())
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
            input.directoryInputs.each { DirectoryInput directoryInput ->
                def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

        }

    }

}