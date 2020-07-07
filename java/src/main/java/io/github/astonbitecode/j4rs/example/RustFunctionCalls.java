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

import org.astonbitecode.j4rs.api.NativeInvocation;
import org.astonbitecode.j4rs.api.ObjectValue;
import org.astonbitecode.j4rs.api.java2rust.Java2RustUtils;

public class RustFunctionCalls {
    private static native void fnnoargs();

    private static native void fnstringarg(NativeInvocation<String> s);

    private static native void fntwoargs(NativeInvocation<String> s, NativeInvocation<Integer> i);

    private static native ObjectValue addintegers(NativeInvocation<Integer> i1, NativeInvocation<Integer> i2);

    private static native ObjectValue throwexception();

    public RustFunctionCalls() throws UnsatisfiedLinkError {
        System.loadLibrary("rustlib");
    }

    public void doCallNoArgs() {
        fnnoargs();
    }

    public void doCallWithStringArg(String s) {
        fnstringarg(Java2RustUtils.createNativeInvocation(s));
    }

    public void doCallWithTwoArgs(String s, Integer i) {
        fntwoargs(Java2RustUtils.createNativeInvocation(s), Java2RustUtils.createNativeInvocation(i));
    }

    public Integer addInRust(Integer i1, Integer i2) {
        ObjectValue objectValue = addintegers(
                Java2RustUtils.createNativeInvocation(i1),
                Java2RustUtils.createNativeInvocation(i2));
        return Java2RustUtils.getObjectCasted(objectValue);
    }

    public void throwExceptionFromRust() {
        throwexception();
    }
}
