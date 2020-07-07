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

import org.astonbitecode.j4rs.errors.InvocationException;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome. This is a simple example that demonstrates calls to rust functions using j4rs.\n");

        var rustFnCalls = new RustFunctionCalls();
        rustFnCalls.doCallNoArgs();
        rustFnCalls.doCallWithStringArg("String created in Java");
        rustFnCalls.doCallWithTwoArgs("String created in Java", 33);
        Integer sum = rustFnCalls.addInRust(1, 2);
        System.out.println("Addition in Rust returned " + sum);
        try {
            rustFnCalls.throwExceptionFromRust();
        } catch (InvocationException ie) {
            System.out.println("Got exception with message: " + ie.getMessage());
        }

        System.out.println("\nBye!");
    }
}
