package com.lyb.besttimer.annotation_compiler;

import com.google.auto.service.AutoService;
import com.lyb.besttimer.annotation_bean.BindInit;
import com.lyb.besttimer.annotation_bean.Constants;
import com.lyb.besttimer.annotation_bean.IAppInit;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class BindInitProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(BindInit.class);
        for (Element element : elementSet) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                TypeSpec typeSpec = poetCreateGetClass(typeElement);
                try {
                    JavaFile.builder(Constants.packageName_init, typeSpec).build().writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                    messager.printMessage(Diagnostic.Kind.WARNING, "失败");
                }
            }
        }
        return false;
    }

    private TypeSpec poetCreateGetClass(TypeElement typeElement) {
        String simpleName = "BindInit_" + typeElement.getQualifiedName().toString().replaceAll("\\.", "_");
        TypeMirror typeMirror = ((DeclaredType) typeElement.getInterfaces().get(0)).getTypeArguments().get(0);
        ClassName initClassName = ClassName.get(IAppInit.class);
        TypeName T = TypeName.get(typeMirror);
        ParameterizedTypeName initTTypeName = ParameterizedTypeName.get(initClassName, T);
        return TypeSpec.classBuilder(simpleName)
                .addSuperinterface(initTTypeName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(poetMethodGetTarget(typeElement, T)).build();
    }

    private MethodSpec poetMethodGetTarget(TypeElement typeElement, TypeName T) {
        return MethodSpec
                .methodBuilder("init")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(T, "context")
                .addStatement("new $T().init(context)", ClassName.get(typeElement)).build();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindInit.class.getCanonicalName());
        return types;
    }
}
