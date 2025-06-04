package ru.slisarenko.springpractick.beanfactorypostprocessors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogBeanFactoryPostProcessors implements BeanFactoryPostProcessor, Ordered {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("this LogBeanFactoryPostProcessors");
        /*for(String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            var beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            var genericArgumentValues = beanDefinition.getConstructorArgumentValues().getGenericArgumentValues() ;
        }*/
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
