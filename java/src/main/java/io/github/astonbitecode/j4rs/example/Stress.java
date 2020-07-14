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

public class Stress {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome. This is a test that tries to stress native calls.\n");
        var rustFnCalls = new RustFunctionCalls();
        stress(rustFnCalls);
        System.out.println("\nBye!");
    }

    private static void stress(RustFunctionCalls rustFnCalls) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            Integer sum = rustFnCalls.addInRust(1, 1);
            if (i % 1000000 == 0) {
                long diff = System.currentTimeMillis() - time;
                System.out.println(i + "---" + diff);
                time = System.currentTimeMillis();
            }
        }
    }
}
