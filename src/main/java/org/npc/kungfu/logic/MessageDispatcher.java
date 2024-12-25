package org.npc.kungfu.logic;

import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.net.IMessageDispatcher;
import org.npc.kungfu.platfame.LoginService;

public class MessageDispatcher implements IMessageDispatcher {

    @Override
    public void dispatchMessage(Object message) {
        if (message instanceof BaseMessage) {
            BaseMessage msg = (BaseMessage) message;
            switch (msg.getMessageType()){
                case LOGIN_MESSAGE:
                    LoginService.onPlayerLogin(msg);
                    break;
                case PLAYER_MESSAGE:
//                    PlayerService.onPlayerLogin(msg);
                    break;
                case MATCH_MESSAGE:
//                    MatchService.onPlayerLogin(msg);
                    break;
                case BATTLE_MESSAGE:
//                    BattleService.onPlayerLogin(msg);
                    break;
                    default:
                        System.out.println("error");
            }
        }
    }
}
