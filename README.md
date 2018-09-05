#sbr-sampling-uk.gov.ons.registers.service

### Prerequisites

* Java 8 or higher
* Gradle

### Development Setup (MacOS)

To install Scala quickly you can use Homebrew ([Brew](http://brew.sh)):
```shell
brew install scala
```

### Running the Service

To compile, build and run the application use the following command:

```shell
./gradlew run --args 'arg1 arg2 arg3'
```

#### Test

To test all test suites we can use:

```shell
gradle test
```

### Integration Tests

For running our integration tests (or acceptance tests) a separate task - itest. To run it do the following;
```shell
gradle itest
```

### Troubleshooting

See [FAQ](FAQ.md) for possible and common solutions.

### Contributing

See [CONTRIBUTING](CONTRIBUTING.md) for details.

### License

Copyright ©‎ 2017, Office for National Statistics (https://www.ons.gov.uk)

Released under MIT license, see [LICENSE](LICENSE.md) for details.
