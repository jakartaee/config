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

package jakarta.config.tck;

import jakarta.config.Config;
import jakarta.config.tck.common.AnyConfiguration;
import jakarta.config.tck.common.JakartaConfigValues;
import jakarta.config.tck.common.My;
import jakarta.config.tck.common.Other;
import jakarta.config.tck.common.TopLevelConfig;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConfigTest {
    @Test
    public void testTopLevelConfig() {
        TopLevelConfig configuration = Config.bootstrap().load(TopLevelConfig.class);
        assertThat(configuration.my().username(), equalTo(JakartaConfigValues.myUserName));
        assertThat(configuration.my().password(), equalTo(JakartaConfigValues.myPassword));
        assertThat(configuration.my().configuration().key(), equalTo(JakartaConfigValues.myConfigurationKey));
        assertThat(configuration.other().configuration().key(), equalTo(JakartaConfigValues.otherConfigurationKey));
    }

    @Test
    public void testSecondLevelMyInterface() {
        My configuration = Config.bootstrap().load(My.class);
        assertThat(configuration.username(), equalTo(JakartaConfigValues.myUserName));
        assertThat(configuration.password(), equalTo(JakartaConfigValues.myPassword));
        assertThat(configuration.configuration().key(), equalTo(JakartaConfigValues.myConfigurationKey));
    }

    @Test
    public void testSecondLevelOtherInterface() {
        Other configuration = Config.bootstrap().load(Other.class);
        assertThat(configuration.configuration().key(), equalTo(JakartaConfigValues.otherConfigurationKey));
    }

    @Test
    public void testOverridePathSecondLevelOtherInterface() {
        Other configuration = Config.bootstrap().path("my").load(Other.class);
        assertThat(configuration.configuration().key(), equalTo(JakartaConfigValues.myConfigurationKey));
    }

    @Test
    public void testThirdLevelAnyConfigurationInterface() {
        AnyConfiguration configuration = Config.bootstrap().load(AnyConfiguration.class);
        assertThat(configuration.key(), equalTo(JakartaConfigValues.myConfigurationKey));
    }

    @Test
    public void testOverridePathThirdLevelAnyConfigurationInterface() {
        AnyConfiguration configuration = Config.bootstrap().path("other", "configuration").load(AnyConfiguration.class);
        assertThat(configuration.key(), equalTo(JakartaConfigValues.otherConfigurationKey));
    }
}
