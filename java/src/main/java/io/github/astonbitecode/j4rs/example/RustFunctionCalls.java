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

import java.util.List;
import java.util.Map;

public class RustFunctionCalls {
    private static native void fnnoargs();

    private static native void fnstringarg(Instance<String> s);

    private static native void fntwoargs(Instance<Integer> i1, Instance<Integer> i2);

    private static native void fnthreeargs(Instance<Integer> i1, Instance<Integer> i2, Instance<Integer> i3);

    private static native Instance addintegers(Instance<Integer> i1, Instance<Integer> i2);

    private static native void fncustomobject(Instance<MyClass> i);

    private static native Instance fncustomobjectret();

    private static native Instance throwexception();

    private static native void fnlist(Instance<List<Integer>> i);
    
    private static native Instance fnmap(Instance<Map<String, Integer>> i);

    private static native Instance callback(Instance<RustFunctionCalls> rfc);

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

    public void doCallWithCustomClass(MyClass myClass) {
        fncustomobject(Java2RustUtils.createInstance(myClass));
    }

    public MyClass doCallWithCustomClassRet() {
        Instance instance = fncustomobjectret();
        return Java2RustUtils.getObjectCasted(instance);
    }

    public void throwExceptionFromRust() {
        throwexception();
    }

    public void doCallWithList(List<Integer> list) {
        fnlist(Java2RustUtils.createInstance(list));
    }

    public Map<String, Integer> doCallWithMap(Map<String, Integer> map) {
        Instance instance = fnmap(Java2RustUtils.createInstance(map));
        return Java2RustUtils.getObjectCasted(instance);
    }

    public void call(Integer i) {
        System.out.println("Java was called by the Rust world! " + i);
    }

    public void testCallback() {
        callback(Java2RustUtils.createInstance(this));
    }
}
