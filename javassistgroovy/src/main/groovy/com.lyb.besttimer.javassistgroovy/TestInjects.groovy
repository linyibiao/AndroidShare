package com.lyb.besttimer.javassistgroovy

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

public class TestInjects {

    private final static ClassPool pool = ClassPool.getDefault()

    public static void inject(String path, Project project) {
        pool.appendClassPath(path)
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        pool.importPackage("android.os.Bundle")

        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath
                println("filePath=" + filePath)
                if (file.name == "MainActivity.class") {
                    CtClass ctClass = pool.getCtClass("com.lyb.besttimer.javassistgroovyapp.MainActivity")
                    println("ctClass=" + ctClass)
                    if (ctClass.isFrozen()) {
                        ctClass.defrost()
                    }
                    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
                    println("方法名=" + ctMethod)
                    String insertBeforeStr = """android.widget.Toast.makeText(this,"我是被插入的Toast代码，最后一次测试哈~!!",android.widget.Toast.LENGTH_SHORT).show();"""
                    ctMethod.insertBefore(insertBeforeStr)
                    ctClass.writeFile(path)
                    ctClass.detach()

                }
            }
        }

    }

}