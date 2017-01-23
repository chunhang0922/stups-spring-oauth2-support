/**
 * Copyright (C) 2016 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.oauth2.spring.authorization;

import org.codehaus.jackson.annotate.JsonProperty;

public class Role {

    @JsonProperty(value = "dn")
    private String dn;

    @JsonProperty(value = "name")
    private String name;

    public String getDn() {
        return dn;
    }

    public void setDn(final String dn) {
        this.dn = dn;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
