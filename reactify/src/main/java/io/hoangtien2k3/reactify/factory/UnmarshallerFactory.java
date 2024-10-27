/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.reactify.factory;

import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * UnmarshallerFactory class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class UnmarshallerFactory {

    private static final ConcurrentHashMap<Class<?>, Unmarshaller> instance = new ConcurrentHashMap<>();

    /**
     * Returns an Unmarshaller instance for the specified class.
     *
     * @param clz the class for which an Unmarshaller is required
     * @return the Unmarshaller instance for the specified class
     */
    public static Unmarshaller getInstance(Class<?> clz) {
        Unmarshaller unmarshaller = instance.get(clz);
        if (unmarshaller != null) return unmarshaller;
        synchronized (instance) {
            unmarshaller = instance.get(clz);
            if (unmarshaller == null) {
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(clz);
                    unmarshaller = jaxbContext.createUnmarshaller();
                    instance.put(clz, unmarshaller);
                } catch (JAXBException e) {
                    throw new RuntimeException("Failed to create Unmarshaller for " + clz.getName(), e);
                }
            }
        }
        return unmarshaller;
    }
}
