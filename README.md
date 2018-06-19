### Functional Programming in Scala
My solutions to the exercises from the excellent book [Functional Programming in Scala](https://github.com/fpinscala/fpinscala)

1. What is functional programming?
2. [Getting started](src/main/scala/Chapter02.scala) => [tests](src/test/scala/Chapter02Spec.scala)
3. [Functional data structures](src/main/scala/Chapter03.scala) => [tests](src/test/scala/Chapter03Spec.scala)
4. [Handling errors without exceptions](src/main/scala/Chapter04.scala) => [tests](src/test/scala/Chapter04Spec.scala)
5. [Strictness and laziness](src/main/scala/Chapter05.scala) => [tests](src/test/scala/Chapter05Spec.scala)
6. [Purely functional state](src/main/scala/Chapter06.scala) => [tests](src/test/scala/Chapter06Spec.scala)
7. [Purely functional parallelism](src/main/scala/Chapter07.scala) => [tests](src/test/scala/Chapter07Spec.scala)
8. [Property-based testing](src/main/scala/Chapter08.scala) => [tests](src/test/scala/Chapter08Spec.scala)

Almost all solutions are covered with corresponding tests.
You can build and run tests and see the coverage by using the following command:

```bash
sbt clean coverage test coverageReport
```
