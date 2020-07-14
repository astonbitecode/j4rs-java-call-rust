/*
 * Copyright 2020 astonbitecode
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
package io.github.astonbitecode.j4rs.example;

import org.astonbitecode.j4rs.api.Instance;
import org.astonbitecode.j4rs.api.java2rust.Java2RustUtils;

public class RustFunctionCalls {
    private static native void fnnoargs();

    private static native void fnstringarg(Instance<String> s);

    private static native void fntwoargs(Instance<Integer> i1, Instance<Integer> i2);

    private static native void fnthreeargs(Instance<Integer> i1, Instance<Integer> i2, Instance<Integer> i3);

    private static native Instance addintegers(Instance<Integer> i1, Instance<Integer> i2);

    private static native Instance throwexception();

    static {
        System.loadLibrary("rustlib");
    }

    public void doCallNoArgs() {
        fnnoargs();
    }

    public void doCallWithStringArg(String s) {
        fnstringarg(Java2RustUtils.createInstance(s));
    }

    public void doCallWithTwoArgs(Integer i1, Integer i2) {
        fntwoargs(Java2RustUtils.createInstance(i1), Java2RustUtils.createInstance(i2));
    }

    public void doCallWithThreeArgs(Integer i1, Integer i2, Integer i3) {
        fnthreeargs(Java2RustUtils.createInstance(i1), Java2RustUtils.createInstance(i2), Java2RustUtils.createInstance(i3));
    }

    public Integer addInRust(Integer i1, Integer i2) {
        Instance instance = addintegers(
                Java2RustUtils.createInstance(i1),
                Java2RustUtils.createInstance(i2));
        return Java2RustUtils.getObjectCasted(instance);
    }

    public void throwExceptionFromRust() {
        throwexception();
    }
}
