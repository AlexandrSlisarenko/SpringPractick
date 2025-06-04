package ru.slisarenko.springpractick.beanpostprocessor.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionalBeanPostProcessor implements BeanPostProcessor {

    private final Logger log = LoggerFactory.getLogger(TransactionalBeanPostProcessor.class);

    private Map<String, Class<?>> naturaleBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Transaction.class)) {
            naturaleBeans.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        var naturalBean = naturaleBeans.get(beanName);
        if (naturalBean != null) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), naturalBean.getInterfaces(), (proxy, method, args) -> {
                log.info("open Transactional bean name = " + beanName);
                try {
                    return method.invoke(bean, args);
                } finally {
                    log.info("close Transactional bean name = " + beanName);
                }
            });
        }
        return bean;
    }
}
