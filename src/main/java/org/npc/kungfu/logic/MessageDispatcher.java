package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.net.IMessageDispatcher;

public class MessageDispatcher implements IMessageDispatcher {

    @Override
    public void dispatchMessage(Object message, Channel senderChannel) {
        if (message instanceof BaseMessage) {
            BaseMessage msg = (BaseMessage) message;
            switch (msg.getMessageType()){
                case LOGIN_MESSAGE:
                    LoginService.getService().onPlayerLogin(msg,senderChannel);
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
