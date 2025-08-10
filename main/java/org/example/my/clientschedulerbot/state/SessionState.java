package org.example.my.clientschedulerbot.state;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class SessionState {
    HashMap<Long,BotState> userState = new HashMap<Long, BotState>();
    HashMap<Long,String> clientName = new HashMap<Long, String>();
    ArrayList<Long> clientIds = new ArrayList<Long>();

    public void  putUser(Long id, BotState state) {
        userState.put(id, state);
    }
    public  void  putClientName(Long id, String name) {
        clientName.put(id, name);
    }
    public String getClientName(Long id) {
        return clientName.get(id);
    }
    public BotState getUser(Long id) {
        return userState.get(id);
    }
    public void clearAll() {
        userState.clear();
        clientName.clear();
    }

}
