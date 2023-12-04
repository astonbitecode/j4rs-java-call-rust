use std::collections::HashMap;
// Copyright 2020 astonbitecode
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
use std::convert::TryFrom;
use std::result::Result;

use j4rs::{InvocationArg, JavaClass};
use j4rs::prelude::*;
use j4rs_derive::*;
use serde::{Deserialize, Serialize};

#[call_from_java("io.github.astonbitecode.j4rs.example.RustSimpleFunctionCall.fnnoargs")]
fn my_function_with_no_args() {
    println!("Hello from the Rust world!");
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.fnstringarg")]
fn my_function_with_1_string_arg(i1: Instance) {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let s: String = jvm.to_rust(i1).unwrap();
    println!("A String Instance was passed to Rust: {}", s);
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.fntwoargs")]
fn my_function_with_2_args(integer_instance1: Instance, integer_instance2: Instance) {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let _i1: i32 = jvm.to_rust(integer_instance1).unwrap();
    let _i2: i32 = jvm.to_rust(integer_instance2).unwrap();
    println!("Instance 1 was '{}' and Instance 2 was: '{}'", _i1, _i2);
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.fnthreeargs")]
fn my_function_with_3_args(integer_instance1: Instance, integer_instance2: Instance, integer_instance3: Instance) {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let _i1: i32 = jvm.to_rust(integer_instance1).unwrap();
    let _i2: i32 = jvm.to_rust(integer_instance2).unwrap();
    let _i3: i32 = jvm.to_rust(integer_instance3).unwrap();
    println!("{}, {}, {}", _i1, _i2, _i3);
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.addintegers")]
fn add_integers(integer_instance1: Instance, integer_instance2: Instance) -> Result<Instance, String> {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let i1: i32 = jvm.to_rust(integer_instance1).unwrap();
    let i2: i32 = jvm.to_rust(integer_instance2).unwrap();
    let sum = i1 + i2;
    let ia = InvocationArg::try_from(sum).map_err(|error| format!("{}", error)).unwrap();
    Instance::try_from(ia).map_err(|error| format!("{}", error))
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.fnlist")]
fn my_function_with_list_arg(list_instance1: Instance) {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let v: Vec<i32> = jvm.to_rust(list_instance1).unwrap();

    println!("Got a list with elements:");
    for i in v {
        println!("{}", i);
    }
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.fnmap")]
fn my_function_with_map_arg(map_instance: Instance) -> Result<Instance, String> {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let m: HashMap<String, i32> = jvm.to_rust(map_instance).unwrap();

    println!("Got a map with elements:");
    for (k, v) in m {
        println!("{}: {}", k, v);
    }

    let map = HashMap::from([
        ("one", 1),
        ("two", 2),
        ("three", 3),
    ]);
    let map_instance_to_return = jvm.java_map(
        JavaClass::String,
        JavaClass::Integer,
        map);
    map_instance_to_return.map_err(|error| format!("{}", error))
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.callback")]
fn test_rust_to_java_callback(callback: Instance) {
    let jvm = Jvm::attach_thread().unwrap();
    let ia = InvocationArg::try_from(33).unwrap();
    let _ = jvm.invoke(&callback, "call", &[ia]).unwrap();
}

#[derive(Deserialize, Serialize, Debug)]
struct MyClass {
    number: i32
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.fncustomobject")]
fn use_custom_object(i: Instance) {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let my_class: MyClass = jvm.to_rust(i).unwrap();
    println!("{:?}", my_class);
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.fncustomobjectret")]
fn ret_custom_object() -> Result<Instance, String> {
    let test_object = MyClass {
        number : 33
    };
    let ia = InvocationArg::new(&test_object, "io.github.astonbitecode.j4rs.example.MyClass");
    return Instance::try_from(ia).map_err(|error| format!("{}", error));
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.throwexception")]
fn returns_error() -> Result<Instance, &'static str> {
    Err("Oops! An error occurred in Rust")
}

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.callrustasync")]
fn call_rust_async(i: Instance) -> Result<Instance, String> {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let number: i64 = jvm.to_rust(i).unwrap();
    // Create a runtime
    let rt = tokio::runtime::Builder::new_current_thread()
        .enable_all()
        .build()
        .unwrap();

    // Call the asynchronous connect method using the runtime.
    let square = rt.block_on(square(number));

    InvocationArg::try_from(square)
        .map_err(|error| format!("{}", error))
        .and_then(|square| Instance::try_from(square).map_err(|error| format!("{}", error)))
}

async fn square(a: i64) -> i64 {
    a * a
}