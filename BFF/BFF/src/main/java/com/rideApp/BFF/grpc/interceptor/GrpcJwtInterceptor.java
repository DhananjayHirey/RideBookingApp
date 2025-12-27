package com.rideApp.BFF.grpc.interceptor;

import io.grpc.*;
import org.springframework.stereotype.Component;
import reactor.util.context.ContextView;
import io.micrometer.context.ContextSnapshot;


@Component
public class GrpcJwtInterceptor implements ClientInterceptor {

    public static final Context.Key<String> AUTH_CONTEXT_KEY =
            Context.key("AUTH_HEADER");

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

                String authHeader = AUTH_CONTEXT_KEY.get();

                if (authHeader != null) {
                    headers.put(AUTHORIZATION_KEY, authHeader);
                }

                super.start(responseListener, headers);
            }
        };
    }
}
