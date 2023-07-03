/*
 * Copyright 2023 astonbitecode
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.astonbitecode.j4rs.example.json;

import io.github.astonbitecode.j4rs.example.MyClass;
import org.astonbitecode.j4rs.api.services.json.Codec;
import org.astonbitecode.j4rs.api.services.json.exceptions.JsonCodecException;

/**
 * A JSON codec that just handles instances of MyClass, to show how to create a plugin that handles the JSON ser/deserialization
 */
public class MyClassJsonCodec implements Codec {

    /**
     * Always creates an instance of {@link MyClass}
     * @param s
     * @param s1
     * @return
     * @param <T>
     * @throws JsonCodecException
     */
    @Override
    public <T> T decode(String s, String s1) throws JsonCodecException {
        return (T) new MyClass(33);
    }

    /**
     * Always return a JSON of MyClass
     * @param o
     * @return
     * @throws JsonCodecException
     */
    @Override
    public String encode(Object o) throws JsonCodecException {
        return "{\"number\":33}";
    }

    @Override
    public Object[] decodeArrayContents(String s) throws JsonCodecException {
        return new Object[0];
    }
}
