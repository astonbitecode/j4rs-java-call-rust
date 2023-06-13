# j4rs-java-call-rust

## Java -> Rust direction examples

[j4rs](https://github.com/astonbitecode/j4rs) focused solely in facilitating Rust applications in making calls to Java code 
by [allowing](https://github.com/astonbitecode/j4rs#basics) 
JVM creation and manipulation from the Rust code, 
effortless Java method invocations,
Java libraries [provisioning via Maven](https://github.com/astonbitecode/j4rs#Using-Maven-artifacts) 
and [more](https://github.com/astonbitecode/j4rs#features).

This has changed since release 0.12.0. 
***j4rs can now be used as well in Java projects that want to achieve JNI calls to Rust libraries.***

The repository contains two projects: One with Java code (using Maven) and one with Rust code (using Cargo).

*Note: For the commands execution, it is assumed that the repository 
is cloned in a directory `/home/myuser/j4rs-java-call-rust`*

## Rust code

### Cargo.toml
The important things here are to add the two needed dependencies (`j4rs` and `j4rs_derive`) 
and mark the project as a `cdylib`, in order to have a shared library as output. 
This library will be loaded and used by the Java code.

### lib.rs
All the functions that we want to be callable from the Java code need to be annotated with the `call_from_java` attribute.
E.g.:

```rust
#[call_from_java("io.github.astonbitecode.j4rs.example.RustSimpleFunctionCall.fnnoargs")]
fn my_function_with_no_args() {
    println!("Hello from the Rust world!");
}
```

The code above, will accept calls from a native method called `fnnoargs`,
defined in the Java class `io.github.astonbitecode.j4rs.example.RustSimpleFunctionCall`.

The `call_from_java` attribute contains the name of the native method as it is defined in Java, along with the package and the Class itself.

### Build the code

While in the */home/myuser/j4rs-java-call-rust/rust* directory, execute `cargo build`. When the compilation completes, 
you should have the `librustlib.so` library under */home/myuser/j4rs-java-call-rust/rust/target/debug/*

## Java code

### pom.xml
We need the `j4rs` 0.15.0 dependency:

```xml
<dependency>
    <groupId>io.github.astonbitecode</groupId>
    <artifactId>j4rs</artifactId>
    <version>0.15.0</version>
</dependency>
```

### Define the native functions
The [RustSimpleFunctionCall](https://github.com/astonbitecode/j4rs-java-call-rust/blob/master/java/src/main/java/io/github/astonbitecode/j4rs/example/RustSimpleFunctionCall.java) 
and [RustFunctionCalls](https://github.com/astonbitecode/j4rs-java-call-rust/blob/master/java/src/main/java/io/github/astonbitecode/j4rs/example/RustFunctionCalls.java) classes 
are examples for loading the shared library generated by Rust and defining the native functions. 

Loading is done with:
```java
static {
    System.loadLibrary("rustlib");
}
```

The native functions are defined simply like following:
```java
private static native void fnnoargs();
private static native void fnstringarg(Instance<String> s);
```

`fnnoargs` in Java maps to `my_function_with_no_args` in Rust.

`fnstringarg` in Java maps to `my_function_with_1_string_arg`

More details on conventions that are in place [here](#Conventions).

### Main class
Simply define a main entry point that instantiates the class that loads the native functions.

### Build and execute
While in the */home/myuser/j4rs-java-call-rust/java* directory, execute `mvn install`. When the compilation completes, 
you should have the `java-calls-rust-1.0-SNAPSHOT.jar` under */home/myuser/j4rs-java-call-rust/java/target/* 
and all the dependencies under */home/myuser/j4rs-java-call-rust/java/target/libs/*

Execute the Main application like following:

```shell script
java -Djava.library.path=/home/myuser/j4rs-java-call-rust/rust/target/debug -jar target/java-calls-rust-1.0-SNAPSHOT.jar 
```

`-Djava.library.path` VM option definition is needed in order for the JVM to locate the `mylib` shared library that is generated by Rust.

The output of the execution should be:
```
Welcome. This is a simple example that demonstrates calls to rust functions using j4rs.

Hello from the Rust world!

Bye!
```

## Conventions

`j4rs` implies some conventions for the arguments and the return types of the native functions.

### Native arguments

In the Rust world, the functions that are accessible from Java must be annotated with the `call_from_java` attribute. 
They can have any number of arguments, but their type must be [j4rs::Instance](https://docs.rs/j4rs/0.12.0/j4rs/struct.Instance.html).

As always, the transformation of `Instance`s to rust values, 
can be achieved with a call to `Jvm.to_rust`:

```rust
let s: String = jvm.to_rust(string_instance)?;
let s: i32 = jvm.to_rust(integer_instance)?;
```

In the Java world, the native methods can have any number of arguments again 
and they must be of type `Instance<T>`. 
A Instance object can be created for any Java Object, 
using the `Java2RustUtils.createInstance` method:

```java
var integerNativeInvocation = Java2RustUtils.createInstance(1);
var stringNativeInvocation = Java2RustUtils.createInstance("My String");
var anObjectNativeInvocation = Java2RustUtils.createInstance(new AnObject());
```

In other words the `j4rs::Instance` type in Rust, 
maps to the `org.astonbitecode.j4rs.api.Instance` type in Java.

### Return types

In the Rust world, the return type of functions can be either `void`, or a `Result` of Instance. 
The `Instances` can be created from an [InvocationArg](https://docs.rs/j4rs/0.12.0/j4rs/enum.InvocationArg.html), 
like following:

```rust
let ia1 = InvocationArg::try_from("a str")?;      // Creates an arg of java.lang.String
let i1 = Instance::try_from(ia1)?				  // Creates an Instance for the ia1 InvocationArg

let ia2 = InvocationArg::try_from(1_i64)?;        // Creates an arg of java.lang.Long
let i2 = Instance::try_from(ia2)?				  // Creates an Instance for the ia1 InvocationArg
```
and be returned to the Java world.

In the Java world, the return type can be either `void` or `Instance<T>`.

In the case that the Rust function returns an `Err`, an `InvocationException` will be thrown in the Java world.

Again, the `j4rs::Instance` type in Rust, maps to the `org.astonbitecode.j4rs.api.Instance` type in Java.
