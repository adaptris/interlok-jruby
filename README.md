# interlok-jruby

[![GitHub tag](https://img.shields.io/github/tag/adaptris/interlok-jruby.svg)](https://github.com/adaptris/interlok-jruby/tags)
[![license](https://img.shields.io/github/license/adaptris/interlok-jruby.svg)](https://github.com/adaptris/interlok-jruby/blob/develop/LICENSE)
[![Actions Status](https://github.com/adaptris/interlok-jruby/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/adaptris/interlok-jruby/actions)
[![codecov](https://codecov.io/gh/adaptris/interlok-jruby/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-jruby)
[![CodeQL](https://github.com/adaptris/interlok-jruby/workflows/CodeQL/badge.svg)](https://github.com/adaptris/interlok-jruby/security/code-scanning)
[![Known Vulnerabilities](https://snyk.io/test/github/adaptris/interlok-jruby/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/adaptris/interlok-jruby?targetFile=build.gradle)
[![Closed PRs](https://img.shields.io/github/issues-pr-closed/adaptris/interlok-jruby)](https://github.com/adaptris/interlok-jruby/pulls?q=is%3Apr+is%3Aclosed)

Use Interlok+JRuby Embed rather than JSR223; and in doing so, allow us to configure ScriptingContainer a bit more than having to use system properties as an all or nothing configuration for all jruby instances.

In addition to specifying the script to execute during `doService()` you can also specify scripts that will be executed as part of
the standard lifecycle `init()/start()/stop()/close()`
