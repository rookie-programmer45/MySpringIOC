package org.ljc.context;

import org.ljc.annotations.MyComponent;
import org.ljc.bean.BeanDefinition;

import java.beans.Introspector;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class ScanSupport {
    /**
     * 扫描指定路径下的bean
     * @param classPath
     * @return key是bean name，value是beanDefinition
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Map<String, BeanDefinition> scan(String classPath) throws IOException, URISyntaxException {
        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
        ClassLoader classLoader = ScanSupport.class.getClassLoader();
        URL resource = classLoader.getResource(classPath.replace(".", "/"));
        Files.walkFileTree(Paths.get(resource.toURI()), new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                File file1 = file.toFile();
                if (file1.isDirectory()) {
                    return FileVisitResult.CONTINUE;
                }
                if (file1.getName().endsWith(".class")) {
                    String absolutePath = file1.getAbsolutePath().replace("\\", "/");
                    String loadClassPath = absolutePath.substring(absolutePath.indexOf("org/"), absolutePath.indexOf(".class"))
                            .replaceAll("/", ".");
                    try {
                        Class<?> beanClass = classLoader.loadClass(loadClassPath);
                        if (beanClass.isAnnotationPresent(MyComponent.class)) {
                            String beanName = getBeanName(beanClass);
                            BeanDefinition beanDefinition = BeanDefinition.buildBeanDefinition(beanClass, beanName);
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                return super.visitFile(file, attrs);
            }
        });
        return beanDefinitionMap;
    }

    /**
     * 从@MyComponent注解中获取bean name，若没有则直接取类简单名
     * @param beanClass
     * @return
     */
    private static String getBeanName(Class<?> beanClass) {
        MyComponent myComponent = beanClass.getAnnotation(MyComponent.class);
        if (myComponent.value() != null && !myComponent.value().equals("")) {
            return myComponent.value();
        }
        String simpleName = beanClass.getSimpleName();
        return Introspector.decapitalize(simpleName);
    }
}
