/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jakarta.config.tck.tests;

import jakarta.config.tck.common.AnyConfiguration;
import jakarta.config.tck.common.JakartaConfigValues;
import jakarta.config.tck.common.My;
import jakarta.config.tck.common.Other;
import jakarta.config.tck.common.TopLevelConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class InjectionTest {
    private static BeanManager beanManager;
    protected static SeContainer container;

    @BeforeAll
    public static void setUp() {
        SeContainerInitializer containerInitializer = SeContainerInitializer.newInstance();
        container = containerInitializer.initialize();
        beanManager = container.getBeanManager();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (container != null && container.isRunning()) {
            container.close();
        }
    }

    @ApplicationScoped
    public static class ConfigurationApplication {
        @Inject
        TopLevelConfig topLevelConfig;

        @Inject
        My my;

        @Inject
        Other other;

        @Inject
        AnyConfiguration anyConfiguration;

        public TopLevelConfig getTopLevelConfig() {
            return topLevelConfig;
        }

        public My getMy() {
            return my;
        }

        public Other getOther() {
            return other;
        }

        public AnyConfiguration getAnyConfiguration() {
            return anyConfiguration;
        }
    }

    @Test
    public void testTopLevelConfig() {
        TopLevelConfig configuration = CDI.current().select(ConfigurationApplication.class).get().getTopLevelConfig();
        assertThat(configuration.my().username(), equalTo(JakartaConfigValues.myUserName));
        assertThat(configuration.my().password(), equalTo(JakartaConfigValues.myPassword));
        assertThat(configuration.my().configuration().key(), equalTo(JakartaConfigValues.myConfigurationKey));
        assertThat(configuration.other().configuration().key(), equalTo(JakartaConfigValues.otherConfigurationKey));
    }

    @Test
    public void testSecondLevelMyInterface() {
        My configuration = CDI.current().select(ConfigurationApplication.class).get().getMy();
        assertThat(configuration.username(), equalTo(JakartaConfigValues.myUserName));
        assertThat(configuration.password(), equalTo(JakartaConfigValues.myPassword));
        assertThat(configuration.configuration().key(), equalTo(JakartaConfigValues.myConfigurationKey));
    }

    @Test
    public void testSecondLevelOtherInterface() {
        Other configuration = CDI.current().select(ConfigurationApplication.class).get().getOther();
        assertThat(configuration.configuration().key(), equalTo(JakartaConfigValues.otherConfigurationKey));
    }

    @Test
    public void testThirdLevelAnyConfigurationInterface() {
        AnyConfiguration configuration = CDI.current().select(ConfigurationApplication.class).get().getAnyConfiguration();
        assertThat(configuration.key(), equalTo(JakartaConfigValues.myConfigurationKey));
    }

}
