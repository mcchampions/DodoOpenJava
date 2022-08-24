package io.github.mcchampions.DodoOpenJava.Event;

import java.util.*;

/**
 * 事件的相关方法（包含监听器注册等）
 */
public class EventManage {
    private static Vector listeners = new Vector();
    public void register(Listener l,String Authorization) {
        listeners.add(l);
        EventTrigger.main(Authorization);
    }
    public void notifyListeners(Event e) {
        for (Listener listener : (Iterable<Listener>) listeners) {
            listener.event(e);
        }
    }
    protected void trigger(Event e) {
        notifyListeners(e);
    }
}
