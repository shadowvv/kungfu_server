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
            switch (msg.getMessageType()) {
                case LOGIN_MESSAGE:
                    LoginService.getService().putMessage(msg, senderChannel);
                    break;
                case PLAYER_MESSAGE:
                    PlayerService.getService().putMessage(msg, senderChannel);
                    break;
                case MATCH_MESSAGE:
                    Player matchPlayer = PlayerService.getService().getPlayer(senderChannel);
                    if (matchPlayer != null) {
                        MatchService.getService().putMessage(msg, matchPlayer);
                    }
                    break;
                case BATTLE_MESSAGE:
                    Player battlePlayer = PlayerService.getService().getPlayer(senderChannel);
                    if (battlePlayer != null) {
                        BattleService.getService().putMessage(msg, battlePlayer);
                    }
                    break;
                default:
                    System.out.println("error");
            }
        }
    }
}
