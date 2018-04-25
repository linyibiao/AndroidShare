package com.lyb.besttimer.java_complier;

import com.google.auto.service.AutoService;
import com.lyb.besttimer.java_annotation.BindClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class BindClassProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeSpec.Builder builder = TypeSpec.classBuilder("Router$$Real")
                .addModifiers(Modifier.PUBLIC);

        ClassName mapClassName = ClassName.get("java.util", "Map");
        ClassName hashmapClassName = ClassName.get("java.util", "HashMap");
        ClassName stringClassName = ClassName.get("java.lang", "String");
        ParameterizedTypeName mapSSTypeName = ParameterizedTypeName.get(mapClassName, stringClassName, stringClassName);

        FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(mapSSTypeName, "bindMap", Modifier.PRIVATE);
        fieldSpecBuilder.initializer(CodeBlock.of("new $T<>()", hashmapClassName));
        builder.addField(fieldSpecBuilder.build());

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(BindClass.class);
        for (Element element : elementSet) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                BindClass bindClass = typeElement.getAnnotation(BindClass.class);
                constructorBuilder.addStatement("bindMap.put($S,$S)", bindClass.path(), typeElement.getQualifiedName().toString());
            }
        }
        builder.addMethod(constructorBuilder.build());

        MethodSpec.Builder findMethodSpecBuilder = MethodSpec
                .methodBuilder("findClass")
                .returns(stringClassName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(stringClassName, "path").build())
                .addStatement("return bindMap.get($N)", "path");
        builder.addMethod(findMethodSpecBuilder.build());

        try {
            JavaFile.builder("com.lyb.besttimer.processor", builder.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
