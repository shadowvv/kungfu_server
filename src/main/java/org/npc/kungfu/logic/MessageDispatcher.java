package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.logic.message.base.BaseServerMessage;
import org.npc.kungfu.net.IMessageDispatcher;

/**
 * 消息分发器
 */
public class MessageDispatcher implements IMessageDispatcher {

    /**
     * 分发器单例
     */
    private static final MessageDispatcher instance = new MessageDispatcher();

    private MessageDispatcher() {
    }

    /**
     * @return 分发器单例
     */
    public static MessageDispatcher getInstance() {
        return instance;
    }

    @Override
    public void dispatchMessage(Object message, Channel senderChannel) {
        if (message instanceof BaseClientMessage) {
            BaseClientMessage clientMessage = (BaseClientMessage) message;
            putMessage(clientMessage, senderChannel);
            return;
        }
        if (message instanceof BaseServerMessage) {
            BaseServerMessage serverMessage = (BaseServerMessage) message;
            putMessage(serverMessage);
        }
    }

    private void putMessage(BaseClientMessage clientMessage, Channel senderChannel) {
        switch (clientMessage.getMessageType()) {
            case LOGIN_MESSAGE: {
                clientMessage.setSenderChannel(senderChannel);
                LoginService.getService().putMessage(clientMessage);
                break;
            }
            case PLAYER_MESSAGE: {
                long playerId = PlayerService.getService().getPlayerId(senderChannel);
                clientMessage.setPlayerId(playerId);
                PlayerService.getService().putMessage(clientMessage);
                break;
            }
            case MATCH_MESSAGE: {
                MatchService.getService().putMessage(clientMessage);
                break;
            }
            case BATTLE_MESSAGE: {
                long playerId = PlayerService.getService().getPlayerId(senderChannel);
                clientMessage.setPlayerId(playerId);
                BattleService.getService().putMessage(clientMessage);
                break;
            }
            default:
                System.out.println("error");
        }
    }

    private void putMessage(BaseServerMessage serverMessage) {
        switch (serverMessage.getMessageType()) {
            case LOGIN_MESSAGE: {
                LoginService.getService().putMessage(serverMessage);
                break;
            }
            case PLAYER_MESSAGE: {
                PlayerService.getService().putMessage(serverMessage);
                break;
            }
            case MATCH_MESSAGE: {
                MatchService.getService().putMessage(serverMessage);
                break;
            }
            case BATTLE_MESSAGE: {
                BattleService.getService().putMessage(serverMessage);
                break;
            }
            default:
                System.out.println("error");
        }
    }

    @Override
    public void dispatchChannelInactiveMessage(Channel channel) {
        LoginService.getService().onChannelInactive(channel);
        PlayerService.getService().onChannelInactive(channel);
    }
}
