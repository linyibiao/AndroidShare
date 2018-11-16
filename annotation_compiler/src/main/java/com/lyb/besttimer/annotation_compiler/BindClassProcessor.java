package com.lyb.besttimer.annotation_compiler;

import com.google.auto.service.AutoService;
import com.lyb.besttimer.annotation_bean.BindClass;
import com.lyb.besttimer.annotation_bean.Constants;
import com.lyb.besttimer.annotation_bean.IGetClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class BindClassProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(BindClass.class);
        for (Element element : elementSet) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                TypeSpec typeSpec = poetCreateGetClass(typeElement);
                try {
                    JavaFile.builder(Constants.packageName_clazz, typeSpec).build().writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private TypeSpec poetCreateGetClass(TypeElement typeElement) {
        BindClass bindClass = typeElement.getAnnotation(BindClass.class);
        String simpleName = "BindClass" + bindClass.path().replaceAll("/", "\\$\\$");
        return TypeSpec.classBuilder(simpleName)
                .addSuperinterface(ClassName.get(IGetClass.class))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(poetMethodGetTarget(typeElement)).build();
    }

    private MethodSpec poetMethodGetTarget(TypeElement typeElement) {
        ClassName classClassName = ClassName.get("java.lang", "Class");
        ParameterizedTypeName classVoidTypeName = ParameterizedTypeName.get(classClassName, WildcardTypeName.subtypeOf(TypeName.OBJECT));
        return MethodSpec
                .methodBuilder("getTargetClass")
                .returns(classVoidTypeName)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $T.class", ClassName.get(typeElement)).build();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindClass.class.getCanonicalName());
        return types;
    }
}
