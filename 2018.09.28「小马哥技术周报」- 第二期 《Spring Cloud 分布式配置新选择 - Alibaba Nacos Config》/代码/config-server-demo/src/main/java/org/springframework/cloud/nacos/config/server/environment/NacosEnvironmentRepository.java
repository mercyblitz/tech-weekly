package org.springframework.cloud.nacos.config.server.environment;

import com.alibaba.nacos.config.server.model.ConfigInfo;
import com.alibaba.nacos.config.server.service.PersistService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static com.alibaba.nacos.config.server.constant.Constants.DEFAULT_GROUP;

public class NacosEnvironmentRepository implements EnvironmentRepository,
        BeanFactoryAware, SmartInitializingSingleton {

    //    @Autowired
    private PersistService persistService;

    private BeanFactory beanFactory;

    @Override
    public Environment findOne(String application, String profile, String label) {
        String dataId = application + "-" + profile + ".properties";
        String groupId = DEFAULT_GROUP;

        String content = getConfig(dataId, groupId);

        Environment environment = new Environment(application, profile);

        Properties properties = createProperties(content);

        environment.add(new PropertySource(dataId, properties));

        return environment;
    }

    private Properties createProperties(String content) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(content));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return properties;
    }

    private String getConfig(String dataId, String groupId) {
//        ConfigInfo configInfo = persistService.findConfigInfo(dataId, groupId, null);
//        return configInfo.getContent();

        return "user.id=1\n user.name=小马哥";
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.persistService = beanFactory.getBean(PersistService.class);
    }
}
