# interlok-jruby 

[![Build Status](https://travis-ci.org/adaptris/interlok-jruby.svg?branch=develop)](https://travis-ci.org/adaptris/interlok-jruby) [![CircleCI](https://circleci.com/gh/adaptris/interlok-jruby/tree/develop.svg?style=svg)](https://circleci.com/gh/adaptris/interlok-jruby/tree/develop) [![codecov](https://codecov.io/gh/adaptris/interlok-jruby/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-jruby) [![Total alerts](https://img.shields.io/lgtm/alerts/g/adaptris/interlok-jruby.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/adaptris/interlok-jruby/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/adaptris/interlok-jruby.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/adaptris/interlok-jruby/context:java) 

Use Interlok+JRuby Embed rather than JSR223; and in doing so, allow us to configure ScriptingContainer a bit more than having to use system properties as an all or nothing configuration for all jruby instances.

In addition to specifying the script to execute during `doService()` you can also specify scripts that will be executed as part of
the standard lifecycle `init()/start()/stop()/close()`
