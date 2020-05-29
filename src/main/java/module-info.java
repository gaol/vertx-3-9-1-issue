module vertx.issue {

    requires vertx.core;
    requires vertx.web;
    requires vertx.bridge.common;

    // Comment this workaround to reproduce the problem:
    requires com.fasterxml.jackson.databind;

    exports issue;
}