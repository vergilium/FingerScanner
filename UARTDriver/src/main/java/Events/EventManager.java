package Events;

import Abstract.IZKPacket;

import java.util.*;

public class EventManager {
    Map<String, List<EventListener>> listeners = new HashMap<>();

    public EventManager(String... operations){
        for(String operation : operations){
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void Subscribe(String eventType, EventListener listener){
        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void Unsubscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void Notify(String eventType, IZKPacket packet) {
        List<EventListener> users = listeners.get(eventType);
        for (EventListener listener : users) {
            listener.update(eventType, packet);
        }
    }
}
