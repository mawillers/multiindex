# Multi-Index-Container

A container class with dynamic indexes. Use this class instead of combining eg. an ArrayList (for iteration
in defined order) with a HashMap (for quick lookup via some key).

## Getting started

Create an instance of MultiIndexContainer and create the indexes you require. Then, put and retrieve data via any of those indexes.
```java
MultiIndexContainer<Employee> container = MultiIndexContainer.create();
SequentialIndex<Employee> bySequence = container.createSequentialIndex();
UniqueIndex<Integer, Employee> byId = container.createHashedUniqueIndex(e -> e.getId());

bySequence.add(new Employee("Jones", 1, "London"));
bySequence.add(new Employee("Miller", 2, "New York"));
bySequence.add(new Employee("Smith", 3, "Berlin"));
bySequence.add(new Employee("Miller", 4, "Bournemouth"));
[...]
boolean wasAdded = bySequence.add(new Employee("Jones", 2, "Austin"));
Assert.isFalse(wasAdded, "Adding this employee violates the uniqueness constraint of byId index");

Optional<Employee> e = byId.getOptional(3);
if (e.isPresent())
    ...
```

### Prerequisites

This software requires Java 8 and Guava.

### Installing

Simply copy the JAR file to your project's CLASSPATH.

## Running the tests

Run `mvn test` for compiling the code and running all unit tests.

## Built with

* [Maven](https://maven.apache.org) - Dependency Management
* [Hamcrest](http://hamcrest.org) - Expressive matchers for JUnit
* [Guava](https://github.com/google/guava) - Generic tools

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Martin Willers** - *Design and implementation*

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* The [Boost Multi-index Containers Library](http://www.boost.org/doc/libs/1_63_0/libs/multi_index/doc/index.html)
  for C++ gave the idea for creating this package.
