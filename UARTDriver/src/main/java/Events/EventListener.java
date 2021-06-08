package Events;

import Abstract.IZKPacket;

public interface EventListener {
    void update(String eventType, IZKPacket file);
}
