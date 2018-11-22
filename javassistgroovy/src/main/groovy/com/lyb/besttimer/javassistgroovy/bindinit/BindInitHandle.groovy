package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.Node
import javassist.*
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

public class BindInitHandle {

    private final static ClassPool pool = ClassPool.getDefault()

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

        List<ClassPath> poolClassPathList = new ArrayList<>()

        AppExtension android = project.extensions.getByType(AppExtension)
        android.bootClasspath.each {
            poolClassPathList.add(pool.appendClassPath(it.absolutePath))
        }

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                poolClassPathList.add(pool.appendClassPath(jarInput.file.absolutePath))
            }
            input.directoryInputs.each { DirectoryInput directoryInput ->
                poolClassPathList.add(pool.appendClassPath(directoryInput.file.absolutePath))
            }
        }

        Set<CtClass> applicationClassList = new HashSet<>()
        android.sourceSets.each {
            File file = it.getManifest().getSrcFile()
            if (file.exists()) {
                GPathResult gPathResult = new XmlSlurper().parse(file)
                String packageName = gPathResult.getProperty("@package")
                Iterator<Node> childIterator = gPathResult.childNodes()
                while (childIterator.hasNext()) {
                    Node childNode = childIterator.next()
                    if (childNode.name() == "application") {
                        String appName = childNode.attributes().get("{http://schemas.android.com/apk/res/android}name")
                        if (appName != null && appName.length() != 0) {
                            if (appName.charAt(0) == ".") {
                                applicationClassList.add(pool.getCtClass(packageName + appName))
                            } else {
                                applicationClassList.add(pool.getCtClass(appName))
                            }
                            break
                        }
                    }
                }
            }
        }

        List<KeyValue> targetClassAndPathList = new ArrayList<>()//目标信息列表
        List<CtClass> initClassList = new ArrayList<>()//需要初始化列表

        CtClass appInitClass = pool.getCtClass("com.lyb.besttimer.annotation_bean.IAppInit")

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                ZipInputStream inputStream = null
                try {
                    inputStream = new ZipInputStream(new FileInputStream(jarInput.file))

                    ZipEntry entry = inputStream.getNextEntry()
                    while (entry != null) {
                        String filePath = entry.getName()
                        if (filePath.endsWith(SdkConstants.DOT_CLASS)) {
                            String classPath = filePath.replace("\\", ".").replace("/", ".")
                            classPath = classPath.substring(0, classPath.length() - SdkConstants.DOT_CLASS.length())
                            try {

                                CtClass ctClass = pool.getCtClass(classPath)
                                CtClass[] interfaces = ctClass.getInterfaces()

                                if (interfaces != null) {
                                    if (interfaces.contains(appInitClass)) {
                                        println("toInit:" + filePath)
                                        initClassList.add(ctClass)
                                    }
                                }
                            } catch (NotFoundException e) {
                                e.printStackTrace()
                            }

                        }
                        entry = inputStream.getNextEntry()
                    }

                } catch (Exception e) {
                    println("jar error path:" + jarInput.file.absolutePath)
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
                    if (filePath.endsWith(SdkConstants.DOT_CLASS)) {
                        String debugStr = "debug"
                        String releaseStr = "release"
                        String classPath
                        //截出正确的类路径
                        if (filePath.contains(debugStr)) {
                            classPath = filePath.substring(filePath.indexOf(debugStr) + debugStr.length() + 1).replace("\\", ".").replace("/", ".")
                        } else {
                            classPath = filePath.substring(filePath.indexOf(releaseStr) + releaseStr.length() + 1).replace("\\", ".").replace("/", ".")
                        }
                        classPath = classPath.substring(0, classPath.length() - SdkConstants.DOT_CLASS.length())
                        while ((classPath.charAt(0) >= '0' && classPath.charAt(0) <= '9') || classPath.charAt(0) == '.') {
                            classPath = classPath.substring(1)
                        }
                        try {
                            CtClass ctClass = pool.getCtClass(classPath)
                            CtClass[] interfaces = ctClass.getInterfaces()

                            if (applicationClassList.contains(ctClass)) {
                                println("target:" + filePath)
                                targetClassAndPathList.add(new KeyValue(ctClass, dirPath))
                            } else if (interfaces != null) {
                                if (interfaces.contains(appInitClass)) {
                                    println("toInit:" + filePath)
                                    initClassList.add(ctClass)
                                }
                            }

                        } catch (NotFoundException e) {
                            println("class error path:" + filePath)
                            e.printStackTrace()
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
        targetClassAndPathList.clear()

        for (CtClass ctClass : initClassList) {
            ctClass.detach()
        }
        initClassList.clear()

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                def jarName = jarInput.name
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

        poolClassPathList.each {
            pool.removeClassPath(it)
        }

    }

}