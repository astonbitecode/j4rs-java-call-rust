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

use j4rs::InvocationArg;
use j4rs::prelude::*;
use j4rs_derive::*;

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

#[call_from_java("io.github.astonbitecode.j4rs.example.RustFunctionCalls.throwexception")]
fn returns_error() -> Result<Instance, &'static str> {
    Err("Oops! An error occurred in Rust")
}
