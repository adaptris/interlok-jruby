require 'jsonpath'
# This should return ["bar"] as the message.
message.setContent(JsonPath.on('{ "foo": "bar" }', '$..foo').to_s, 'UTF-8')