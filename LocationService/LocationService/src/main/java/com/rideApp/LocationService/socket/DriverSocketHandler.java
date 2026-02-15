package com.rideApp.LocationService.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideApp.LocationService.model.LocationUpdatePayload;
import com.rideApp.LocationService.repository.DriverLocationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DriverSocketHandler extends TextWebSocketHandler {

    private final DriverLocationRepository repo;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public DriverSocketHandler(DriverLocationRepository repo) {
        this.repo = repo;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String driverId = getDriverIdFromQuery(session);
        sessions.put(driverId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        LocationUpdatePayload payload =
                mapper.readValue(message.getPayload(),
                        LocationUpdatePayload.class);

        repo.updateLocation(
                payload.getDriverId(),
                payload.getLat(),
                payload.getLng(),
                payload.isAvailable()
        );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) {

        String driverId = getDriverIdFromQuery(session);
        sessions.remove(driverId);
        repo.updateLocation(driverId, 0, 0, false);
    }

    public void sendMessageToDriver(String driverId, String message)
            throws IOException {

        WebSocketSession session = sessions.get(driverId);

        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    private String getDriverIdFromQuery(WebSocketSession session) {

        URI uri = session.getUri();

        if (uri == null || uri.getQuery() == null) {
            return null;
        }

        String query = uri.getQuery(); // driverId=123

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("driverId")) {
                return pair[1];
            }
        }

        return null;
    }

}
