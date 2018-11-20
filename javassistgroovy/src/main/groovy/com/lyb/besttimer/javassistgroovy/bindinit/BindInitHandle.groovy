package com.lyb.besttimer.javassistgroovy.bindinit

import com.android.SdkConstants
import com.android.build.api.transform.DirectoryInput
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

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

    static void inject(Collection<DirectoryInput> directoryInputs) {

        CtClass appClass = pool.getCtClass("android.app.Application")
        List<KeyValue> targetClassAndPathList = new ArrayList<>()//目标信息列表
        List<CtClass> initClassList = new ArrayList<>()//需要初始化列表

        for (DirectoryInput directoryInput : directoryInputs) {
            String dirPath = directoryInput.file.absolutePath
            insertClassPath(dirPath)
            File dir = new File(dirPath)
            if (dir.isDirectory()) {
                dir.eachFileRecurse { File file ->
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
                        if (appClass == ctClass.getSuperclass() || appClass == ctClass) {
                            targetClassAndPathList.add(new KeyValue(ctClass, dirPath))
                        } else if (interfaces != null) {
                            if (interfaces.contains(pool.getCtClass("com.lyb.besttimer.annotation_bean.IAppInit"))) {
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

    }

}