package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.net.IMessageDispatcher;

public class MessageDispatcher implements IMessageDispatcher {

    @Override
    public void dispatchMessage(Object message, Channel senderChannel) {
        if (message instanceof BaseMessage) {
            BaseMessage msg = (BaseMessage) message;
            msg.setSenderChannel(senderChannel);
            switch (msg.getMessageType()) {
                case LOGIN_MESSAGE:
                    LoginService.getService().putMessage(msg);
                    break;
                case PLAYER_MESSAGE: {
                    int playerId = LoginService.getService().getPlayerId(senderChannel);
                    msg.setPlayerId(playerId);
                    PlayerService.getService().putMessage(msg);
                    break;
                }
                case MATCH_MESSAGE: {
                    int playerId = LoginService.getService().getPlayerId(senderChannel);
//                    MatchService.getService().putMessage(msg, playerId);
                    break;
                }
                case BATTLE_MESSAGE: {
                    int playerId = LoginService.getService().getPlayerId(senderChannel);
                    msg.setPlayerId(playerId);
                    BattleService.getService().putMessage(msg);
                    break;
                }
                default:
                    System.out.println("error");
            }
        }
    }
}
