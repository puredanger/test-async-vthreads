# Test harness for core.async vthreads

This repo has a simple program in src and a build program and deps.edn aliases to compile and run from a variety of options.

## Run from source

```
clj -M:test
```

When run, the program will print "Running main" and then print the thread type used for both go blocks and io-threads (these should be the same). 

Note that if the go block reports a VirtualThread, this still doesn't tell you whether you're running an IOC state machine go block or a vthread go block. If running a compiled go block, checks the emitted classes.

## Compile

To compile:

```
clj -T:build co
```

To compile with a specific vthreads property set during compilation:

```
clj -T:build co :vthreads '"target"'
```

You can examine the emitted classes to see how it was compiled. If you see state_machine classes, it was IOC compiled:

```
$ ls target/classes | grep main
main$_main$fn__8321$G__8316__8322.class
main$_main$fn__8321$state_machine__5674__auto____8324$fn__8326.class
main$_main$fn__8321$state_machine__5674__auto____8324.class
...
```

If not, it was compiled to a vthread-style go block.

## Run from compiled (no source)

```
clj -M:test:nosrc
```

## Set vthreads property

By default, the `clojure.core.async.vthreads` system property is unset, use these aliases to change that:

* `:vtarget` - sets `-Dclojure.core.async.vthreads=target`
* `:vavoid` - sets `-Dclojure.core.async.vthreads=avoid`

These aliases can be combined with the aliases above ala `clj -M:test:nosrc:vtarget`.

## JVM version

Another factor in behavior is which JVM is the current runtime. For Java 21+, virtual threads are available, for Java <21, virtual threads are not available. You'll need to control this from the shell by setting your PATH appropriately.


