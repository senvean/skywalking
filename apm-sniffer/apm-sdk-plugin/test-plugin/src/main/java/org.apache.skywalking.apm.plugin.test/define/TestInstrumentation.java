/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.skywalking.apm.plugin.test.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.MultiClassNameMatch;

import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;
import static org.apache.skywalking.apm.agent.core.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;

public class TestInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    public static final String ENHANCE_CLASS = "com.example.app.service.AppClientAdapter";

    public static final String INTERCEPTOR_CLASS = "org.apache.skywalking.apm.plugin.test.TestInterceptor";

    public static final String ENHANCE_METHOD_DISPATCH = "test";

    @Override
    protected ClassMatch enhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(ENHANCE_CLASS);
    }

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        InstanceMethodsInterceptPoint[] instanceMethodsInterceptPoints = new InstanceMethodsInterceptPoint[1];
        instanceMethodsInterceptPoints[0] = new InstanceMethodsInterceptPoint() {
            @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                return nameStartsWith(ENHANCE_METHOD_DISPATCH)
                        .and(takesArgumentWithType(0,"java.lang.String"));
            }

            @Override public String getMethodsInterceptor() {
                return INTERCEPTOR_CLASS;
            }

            @Override public boolean isOverrideArgs() {
                return false;
            }
        };
        return instanceMethodsInterceptPoints;
    }
}
