//package com.example.reactiveprogramminginsprong5;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.HttpHandler;
//import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.netty.http.server.HttpServer;
//
//import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
//import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//
//class StandaloneApplication { // (1)
//    public static void main(String[] args) { // (2)
//        HttpHandler httpHandler = RouterFunctions.toHttpHandler( // (2.1)
//                routes(new BCryptPasswordEncoder(18)) // (2.2)
//        ); //
//        ReactorHttpHandlerAdapter reactorHttpHandler = // (2.3)
//                new ReactorHttpHandlerAdapter(httpHandler); //
//        HttpServer.
//                create() // (3)
//                .port(8080) // (3.1)
//                .handle(reactorHttpHandler) // (3.2)
//                .bind() // (3.3)
//                .flatMap(DisposableChannel::onDispose) // (3.4)
//                .block(); //
//    } //
//    static RouterFunction<ServerResponse> routes( // (4)
//                                                  PasswordEncoder passwordEncoder //
//    ){ //
//        return
//                route(POST("/check"), // (5)
//                        request -> request //
//                                .bodyToMono(PasswordDTO.class) // (5.1)
//                                .map(p -> passwordEncoder //
//                                        .matches(p.getRaw(), p.getSecured())) // (5.2)
//                                .flatMap(isMatched -> isMatched // (5.3)
//                                        ? ServerResponse //
//                                        .
//                                        ok() //
//                                        .build() //
//                                        : ServerResponse //
//                                        .
//                                        status(HttpStatus.EXPECTATION_FAILED) //
//                                        .build() //
//                                ) //
//                ); //
//    }
//}