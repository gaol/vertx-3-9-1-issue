# SockJS bridge broken in Vert.x 3.9.0 / 3.9.1

The project already contains the workaround which is:
 
```
requires com.fasterxml.jackson.databind;
```

in module-info.java

So first thing is to check the project is running correctly with this workaround:

- Run issue.IssueVerticle
- Open http://localhost:8080
- Check the echo appears in the server output (should be {"name":"tim","age":587}) 

Then, how to reproduce the problem:
- Comment the workaround in the module-info.java
- Run issue.IssueVerticle
- Open http://localhost:8080
- There should be now 2 problems:
    - no echo appearing in the server output
    - Error in browser console: GET http://localhost:8080/eventbus/info 500 (Internal Server Error) 
- Also the internal exception can be intercepted by putting a breakpoint at io.vertx.core.streams.impl.InboundBuffer line 239
```
  private <T> void handleEvent(Handler<T> handler, T element) {
    if (handler != null) {
      try {
        handler.handle(element);
      } catch (Throwable t) {
        handleException(t); // <= breakpoint here
      }
    }
  }
```