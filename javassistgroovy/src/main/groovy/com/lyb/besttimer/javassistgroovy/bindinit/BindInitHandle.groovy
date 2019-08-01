package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.Node
import javassist.*
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
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

        String appInitRegisterPath = null//目标路径
        List<CtClass> initClassList = new ArrayList<>()//需要初始化列表

        CtClass appInitClass = pool.getCtClass("com.lyb.besttimer.annotation_bean.IAppInit")
        CtClass appInitRegisterClass = pool.getCtClass("com.lyb.besttimer.annotation_bean.AppInitRegister")
        CtClass contextClass = pool.getCtClass("android.content.Context")

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->

                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                File dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)

                ZipInputStream inputStream = null
                try {
                    inputStream = new ZipInputStream(new FileInputStream(dest))

                    ZipEntry entry = inputStream.getNextEntry()
                    while (entry != null) {
                        String filePath = entry.getName()
                        if (filePath.endsWith(SdkConstants.DOT_CLASS)) {
                            String classPath = filePath.replace("\\", ".").replace("/", ".")
                            classPath = classPath.substring(0, classPath.length() - SdkConstants.DOT_CLASS.length())
                            try {

                                CtClass ctClass = pool.getCtClass(classPath)
                                CtClass[] interfaces = ctClass.getInterfaces()

                                if (appInitRegisterClass == ctClass) {
                                    println("target:" + filePath)
                                    appInitRegisterPath = dest.absolutePath
                                } else if (interfaces != null) {
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
                    println("jar error path:" + dest)
                    e.printStackTrace()
                } finally {
                    if (inputStream != null) {
                        inputStream.close()
                    }
                }
            }
            input.directoryInputs.each { DirectoryInput directoryInput ->

                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)

                String dirPath = dest.absolutePath
                List<File> childFiles = getAllFiles(dest)
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

                            if (appInitRegisterClass == ctClass) {
                                println("target:" + filePath)
                                appInitRegisterPath = dirPath
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

        String insertInitStr = ""
        for (CtClass oneInitClass : initClassList) {
            if (oneInitClass.isFrozen()) {
                oneInitClass.defrost()
            }
            println('initClass here:' + oneInitClass.getName())
            insertInitStr += "new " + oneInitClass.getName() + "().init(applicationContext);"
        }
        if (appInitRegisterClass.isFrozen()) {
            appInitRegisterClass.defrost()
        }
        CtClass[] ctClasses = new CtClass[1]
        ctClasses[0] = contextClass
        CtMethod registerMethod = appInitRegisterClass.getDeclaredMethod("register", ctClasses)
        registerMethod.insertAfter(insertInitStr)
        if (appInitRegisterPath.endsWith(".jar")) {
            File jarFile = new File(appInitRegisterPath)
            def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
            if (optJar.exists())
                optJar.delete()
            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = file.getInputStream(jarEntry)
                jarOutputStream.putNextEntry(zipEntry)

                String entryClassPath = entryName.replace("\\", ".").replace("/", ".")
                entryClassPath = entryClassPath.substring(0, entryClassPath.length() - SdkConstants.DOT_CLASS.length())

                try {
                    CtClass entryClass = pool.getCtClass(entryClassPath)

                    if (appInitRegisterClass == entryClass) {
                        println('generate code into:' + entryName)
                        jarOutputStream.write(appInitRegisterClass.toBytecode())
                    } else {
                        jarOutputStream.write(IOUtils.toByteArray(inputStream))
                    }
                } catch (NotFoundException e) {
                    println("class error path:" + entryClassPath)
                    e.printStackTrace()
                }
                inputStream.close()
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            file.close()

            if (jarFile.exists()) {
                jarFile.delete()
            }
            optJar.renameTo(jarFile)
        } else {
            appInitRegisterClass.writeFile(appInitRegisterPath)
        }

        appInitRegisterClass.detach()

        for (CtClass ctClass : initClassList) {
            ctClass.detach()
        }
        initClassList.clear()

        poolClassPathList.each {
            pool.removeClassPath(it)
        }

    }

}