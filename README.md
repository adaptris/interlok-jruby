[![Codacy Badge](https://api.codacy.com/project/badge/Grade/45243f7bd5d04296b005f93328c58405)](https://www.codacy.com/app/quotidian-ennui/interlok-jruby?utm_source=github.com&utm_medium=referral&utm_content=adaptris/interlok-jruby&utm_campaign=badger)
# interlok-jruby [![Build Status](https://travis-ci.org/adaptris/interlok-jruby.svg?branch=develop)](https://travis-ci.org/adaptris/interlok-jruby) [![codecov](https://codecov.io/gh/adaptris/interlok-jruby/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-jruby)

Use Interlok+JRuby Embed rather than JSR223; and in doing so, allow us to configure ScriptingContainer a bit more than having to use system properties as an all or nothing configuration for all jruby instances.

In addition to specifying the script to execute during `doService()` you can also specify scripts that will be executed as part of
the standard lifecycle `init()/start()/stop()/close()`
