[![Clojars Project](https://img.shields.io/clojars/v/jmh-clojure/task.svg)](https://clojars.org/jmh-clojure/task)
[![](https://github.com/jgpc42/jmh-clojure-task/workflows/Test%20runner/badge.svg)][ci]

### Dependency and version information
<details>
  <summary>Click to show</summary>

[Leiningen][lein]

``` clojure
[jmh-clojure/task "0.1.1"]
```

[tools.deps][deps]

```clojure
{jmh-clojure/task {:mvn/version "0.1.1"}}
```

[Maven](http://maven.apache.org)

``` xml
<dependency>
  <groupId>jmh-clojure</groupId>
  <artifactId>task</artifactId>
  <version>0.1.1</version>
</dependency>
```

JDK versions 8 to 16 and Clojure versions 1.7 to 1.10 are currently [tested against][ci].
</details>

### What is it?

Various convenience utilities for [jmh-clojure][jmh-clj] intended to be used by higher-level tools. Note that while the examples below use `tools.deps`, this library can also be used via a [`Leiningen plugin`][lein-jmh], or standalone, as well.

### Usage

For `clj`, the recommend way to use this library is by adding an alias to your `deps.edn`. The following will be sufficient for most users:

```clojure
{#_...
 :aliases
 {:jmh {:extra-paths ["classes"]
        :extra-deps {jmh-clojure/task {:mvn/version "0.1.1"}}
        :ns-default jmh.task
        :exec-fn jmh.task/run}}}
```

It's important to notice that the `"classes"` directory is added to the project paths. This is required for `jmh-clojure` since it generates `.class` files dynamically (written to `*compile-path*`, by default). We must add this output directory so it's available on the classpath when running.

By default, a `jmh.edn` file at the root of your project is used to configure the benchmark runner. Please see the [sample file][sample] for a complete guide. For example, the following runs all benchmarks, enables the standard JMH status log, and outputs pretty-printed benchmark result data:

```bash
$ mkdir -p classes
$ clj -X:jmh :format :pprint, :status true
```

(If you're new to the `-X` flag, it allows us to omit the Clojure map curly braces (`{}`) on the command line.)

Notice again that we must manually create the `classes` directory before running. Ensuring the `*compile-path*` directory exists is not automated by `tools.deps` as it is with other tools like Leiningen.

### Available options

The optional map to `jmh.task/run` specifies the normal jmh-clojure run [options][run-doc]. The extra task options provided by this library include:

| Option        | Description                                    |
| ------------- | ---------------------------------------------- |
| `:exclude`    | keys to remove from each benchmark result map. |
| `:file`       | read the given file(s) instead of `jmh.edn`.   |
| `:format`     | print results in the given format(s).          |
| `:only`       | keys to select from each benchmark result map. |
| `:output`     | specify the location to write the results.     |
| `:pprint`     | equivalent to `:format :pprint`.               |
| `:progress`   | display progress data while running.           |
| `:sort`       | key(s) to order the results by.                |

Please run `clj -X:jmh help` for more information on the available options. Note that some formats (e.g., `:table`) exclude some result information due to space considerations. See the previously mentioned help for details.

The available JMH profilers may be listed with: `clj -X:jmh profilers`.

### Uberjar example

Along with integrating with the Clojure project tool of your choice, you can also create a standalone jar file. This is useful if you intend to run benchmarks on a different machine than your development one and want to package everything up so you don't have to re-create the project build environment. The `jmh.main` namespace is designed to be used as the jar entry point.

For Clojure tools, we'll use the [`uberdeps`][udeps] library to create the jar. In the root of a hypothetical project:

```bash
mkdir -p classes uberdeps
echo '{:deps {uberdeps/uberdeps {:mvn/version "1.0.4"}}}' > uberdeps/deps.edn
clj -Ajmh -M -e "(compile 'jmh.main)"
cd uberdeps
clj -M -m uberdeps.uberjar --aliases jmh --deps-file ../deps.edn --main-class jmh.main --target ../target/jmh.jar
mkdir classes
java -cp classes:../target/jmh.jar jmh.main :help
```

Note again that even when running as an uberjar, we still need to create the `classes` directory beforehand to enable the dynamic compilation.
Running with `-Dfile.encoding=UTF-8` is also advisable depending on your platform due to the unicode characters JMH can output.

The procedure for other tools should be similarly straightforward.

### More information

Please see the [`jmh-clojure`][jmh-clj] project for everything else about jmh-clojure.

### Running the tests

```bash
lein test
```

Or, `lein test-all` for all supported Clojure versions.

### License

Copyright © 2017-2021 Justin Conklin

Distributed under the Eclipse Public License, the same as Clojure.



[ci]:        https://github.com/jgpc42/jmh-clojure-task/blob/master/.github/workflows/test.yml
[deps]:      https://github.com/clojure/tools.deps.alpha
[jmh-clj]:   https://github.com/jgpc42/jmh-clojure
[lein]:      http://github.com/technomancy/leiningen
[lein-jmh]:  https://github.com/jgpc42/lein-jmh
[run-doc]:   https://jgpc42.github.io/jmh-clojure/doc/jmh.core.html#var-run
[sample]:    https://github.com/jgpc42/jmh-clojure/blob/master/resources/sample.jmh.edn
[udeps]:     https://github.com/tonsky/uberdeps
