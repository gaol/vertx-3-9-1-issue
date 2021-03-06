package issue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * @author Bruno Salmon
 */
public class IssueVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        VertxRunner.runVerticle(IssueVerticle.class);
    }

    @Override
    public void start() {
        // Creating web server and its router
        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setPort(8080)
                ;
        HttpServer server = vertx.createHttpServer(httpServerOptions);
        Router router = Router.router(vertx);

        // Logging web requests
        router.route("/*").handler(LoggerHandler.create());

        // SockJS event bus bridge
        router.mountSubRouter("/eventbus", SockJSHandler.create(vertx)
                .bridge(new SockJSBridgeOptions()
                                .addInboundPermitted(new PermittedOptions(new JsonObject()))
                                .addOutboundPermitted(new PermittedOptions(new JsonObject()))
                )
        );

        vertx.eventBus().consumer("echo", event -> System.out.println(event.body()));

        // Serving static files under the webroot folder
        router.route("/*").handler(StaticHandler.create());

        // Binding the web port
        server.requestHandler(router).listen();
    }
}
