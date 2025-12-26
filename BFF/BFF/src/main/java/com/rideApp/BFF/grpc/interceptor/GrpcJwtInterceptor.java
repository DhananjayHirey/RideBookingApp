package com.rideApp.BFF.grpc.interceptor;

import io.grpc.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;
@Component
public class GrpcJwtInterceptor implements ClientInterceptor {

    static final Metadata.Key<String> AUTHORIZATION_KEY =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next
    ) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(
                next.newCall(method, callOptions)
        ) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {

                // Pull JWT from Reactor Context
                ContextView reactorContext =
                        reactor.util.context.Context.of(Context.current());

                if (reactorContext.hasKey("AUTH_HEADER")) {
                    String authHeader = reactorContext.get("AUTH_HEADER");
                    if (authHeader != null) {
                        headers.put(AUTHORIZATION_KEY, authHeader);
                    }
                }

                super.start(responseListener, headers);
            }
        };
    }
}
