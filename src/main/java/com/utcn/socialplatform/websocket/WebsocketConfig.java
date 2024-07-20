package com.utcn.socialplatform.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.utcn.socialplatform.model.Constants.*;

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint(API_V1+SOCKET+"/notification")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:3003")
                .withSockJS();

        stompEndpointRegistry.addEndpoint(API_V1+SOCKET+"/messages")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:3003")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
    }

}