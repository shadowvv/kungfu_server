package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.net.IMessageDispatcher;

public class MessageDispatcher implements IMessageDispatcher {

    private static final MessageDispatcher instance = new MessageDispatcher();

    private MessageDispatcher() {
    }

    public static MessageDispatcher getInstance() {
        return instance;
    }

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
                    long playerId = PlayerService.getService().getPlayerId(senderChannel);
                    msg.setPlayerId(playerId);
                    PlayerService.getService().putMessage(msg);
                    break;
                }
                case MATCH_MESSAGE: {
                    MatchService.getService().putMessage(msg);
                    break;
                }
                case BATTLE_MESSAGE: {
                    long playerId = PlayerService.getService().getPlayerId(senderChannel);
                    msg.setPlayerId(playerId);
                    BattleService.getService().putMessage(msg);
                    break;
                }
                default:
                    System.out.println("error");
            }
        }
    }

    @Override
    public void dispatchChannelInactiveMessage(Channel channel) {
        LoginService.getService().onChannelInactive(channel);
        PlayerService.getService().onChannelInactive(channel);
    }
}
