# interlok-jruby [![Build Status](https://travis-ci.org/adaptris/interlok-jruby.svg?branch=develop)](https://travis-ci.org/adaptris/interlok-jruby) [![codecov](https://codecov.io/gh/adaptris/interlok-jruby/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-jruby)

Use Interlok+JRuby Embed rather than JSR223; and in doing so, allow us to configure ScriptingContainer a bit more than having to use system properties for everything.

In addition to specifying the script to execute during `doService()` you can also specify scripts that will be executed as part of
the standard lifecycle `init()/start()/stop()/close()`
