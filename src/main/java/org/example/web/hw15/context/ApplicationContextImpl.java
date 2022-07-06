package org.example.web.hw15.context;

import lombok.SneakyThrows;
import org.example.web.hw15.annotation.LikeBean;
import org.example.web.hw15.context.ApplicationContext;
import org.example.web.hw15.exception.NoSuchBeanException;
import org.example.web.hw15.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationContextImpl implements ApplicationContext {
    private Map<String, Object> beansContainer = new HashMap<>();

    /**
     * 1. Scan the package to find all classes annotated with @LikeBean
     * 2. Create instances of those classes
     * 3. Resolve a name for each bean
     *  i. If annotation has name like this @LikeBean("coolBean") – use "coolBean"
     *  ii. Otherwise, use class name with the lowercased first letter. E.g. for PrinterService class – use "printerService"
     * 4. Store created object by its name in the application context
     * @param packageName
     */
    public ApplicationContextImpl(String packageName) {
        var reflections = new Reflections(packageName);
        var classesAnnotatedWithLikeBean = reflections.getTypesAnnotatedWith(LikeBean.class);
        for (var aClass : classesAnnotatedWithLikeBean) {
          beansContainer.put(resolveName(aClass), createInstance(aClass));
        }
    }

    @SneakyThrows
    private Object createInstance(Class<?> type) {
        var constructor = type.getConstructor();
        return type.cast(constructor.newInstance());
    }

    private String resolveName(Class<?> type) {
        var annotationValue = type.getAnnotation(LikeBean.class).value();
        return annotationValue.isBlank() ? type.getSimpleName() : annotationValue;
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        var beanStreams = beansContainer.values().stream()
                .filter(b -> b.getClass().isAssignableFrom(beanType))
                .map(beanType::cast);
        if (beanStreams.count() > 1) {
            throw new NoUniqueBeanException();
        } else {
            return beanStreams.findAny().orElseThrow(NoSuchBeanException::new);
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
        var bean = beansContainer.get(name);
        if (bean != null) {
            return beanType.cast(bean);
        } else {
            throw new NoSuchBeanException();
        }
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beansContainer.entrySet().stream()
                .filter(b -> b.getValue().getClass().isAssignableFrom(beanType))
                .collect(Collectors.toMap(Map.Entry::getKey, b -> beanType.cast(b.getValue())));
    }
}
