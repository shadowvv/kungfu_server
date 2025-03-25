package org.npc.kungfu;

import org.apache.ibatis.session.SqlSession;
import org.npc.kungfu.database.DruidDataSourceFactory;
import org.npc.kungfu.database.GameInfoEntity;
import org.npc.kungfu.database.GameInfoMapper;
import org.npc.kungfu.database.MyBatisUtils;
import org.npc.kungfu.logic.*;
import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.match.MatchPoolSelector;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.net.WebSocketService;
import org.npc.kungfu.platfame.bus.*;
import org.npc.kungfu.redis.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class Booster {

    private static final Logger logger = LoggerFactory.getLogger(Booster.class);
    private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;
    private static final int INITIAL_DELAY = 100;
    private static final int TICK_PERIOD = 30;

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting Booster with the following parameters:");
        logger.info("Number of available processors: {}", Runtime.getRuntime().availableProcessors());
        logger.info("Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
        logger.info("Java Version: {}", System.getProperty("java.version"));

        // 添加: 初始化 MyBatis
        MyBatisUtils.init();
        logger.info("MyBatis initialized successfully.");
        // 添加: 初始化 Druid 数据源
        DruidDataSourceFactory.init();
        logger.info("Druid DataSource initialized successfully.");
        // 添加: 初始化 Jedis
        JedisUtil.init();
        logger.info("Jedis initialized successfully.");

        GameInfoEntity gameInfoEntity = null;
        try (SqlSession session = MyBatisUtils.getSession()) {
            GameInfoMapper gameInfoMapper = session.getMapper(GameInfoMapper.class);
            gameInfoEntity = gameInfoMapper.getGameInfo();
            session.commit();
        } catch (Exception e) {
            logger.error("Failed to get game info: {}", e.getMessage());
            return;
        }

        initializeMainSingleThread();
        initializeLoginService(gameInfoEntity.getNextPlayerId());
        initializePlayerService();
        initializeMatchService();
        initializeBattleService();
        initializeWebSocketService();

        logger.info("server started successfully.");
    }

    private static void initializeMainSingleThread() {
        logger.info("Initializing Main Single Thread...");
        SimpleBusStation<BaseMessage> mainStation = new SimpleBusStation<>(1, "login", new Function<Integer, SimplePassenger<BaseMessage>>() {
            @Override
            public SimplePassenger<BaseMessage> apply(Integer integer) {
                return new MainSimplePassenger<>(integer);
            }
        });
        StationDriver mainDriver = new StationDriver(mainStation, INITIAL_DELAY, TICK_PERIOD);
        mainDriver.runStation();
        logger.info("Main Single Thread initialized successfully.");
    }

    private static void initializeLoginService(int nextPlayerId) {
        logger.info("Initializing Login Service with THREAD_NUM: {}, MAX_QUEUE_SIZE: {}, TIMEOUT: {}...", THREAD_NUM, INITIAL_DELAY, TICK_PERIOD);
        SimpleBusStation<BaseMessage> loginStation = new SimpleBusStation<>(THREAD_NUM, "login");
        StationDriver loginDriver = new StationDriver(loginStation, INITIAL_DELAY, TICK_PERIOD);
        loginDriver.runStation();
        LoginService.getService().init(loginStation, nextPlayerId);
        logger.info("Login Service initialized successfully.");
    }

    private static void initializePlayerService() {
        logger.info("Initializing Player Service with THREAD_NUM: {}, MAX_QUEUE_SIZE: {}, TIMEOUT: {}...", THREAD_NUM, INITIAL_DELAY, TICK_PERIOD);
        BusStation<Bus<Player, BaseMessage>, Player, BaseMessage> playerStation = new BusStation<>(THREAD_NUM, "player", new BusHashSelector<>());
        for (int i = 0; i < THREAD_NUM; i++) {
            playerStation.put(new Bus<>(i, "player"));
        }
        StationDriver playerDriver = new StationDriver(playerStation, INITIAL_DELAY, TICK_PERIOD);
        playerDriver.runStation();
        PlayerService.getService().init(playerStation);
        logger.info("Player Service initialized successfully.");
    }

    private static void initializeMatchService() {
        logger.info("Initializing Match Service with THREAD_NUM: {}, MAX_QUEUE_SIZE: {}, TIMEOUT: {}...", THREAD_NUM, INITIAL_DELAY, TICK_PERIOD);
        BusStation<SoloPassengerBus<MatchPool, BaseMessage>, MatchPool, BaseMessage> matchStation = new BusStation<>(1, "match", new MatchPoolSelector());
        for (int i = 0; i < 1; i++) {
            MatchPool matchPool = new MatchPool(i, "match");
            matchStation.put(new SoloPassengerBus<>(i, "match", matchPool));
        }
        StationDriver matchDriver = new StationDriver(matchStation, INITIAL_DELAY, TICK_PERIOD);
        matchDriver.runStation();
        MatchService.getService().init(matchStation);
        logger.info("Match Service initialized successfully.");
    }

    private static void initializeBattleService() {
        logger.info("Initializing Battle Service with THREAD_NUM: {}, MAX_QUEUE_SIZE: {}, TIMEOUT: {}...", THREAD_NUM, INITIAL_DELAY, TICK_PERIOD);
        BusStation<Bus<BattleRing, BaseMessage>, BattleRing, BaseMessage> battleStation = new BusStation<>(THREAD_NUM, "battle", new BusHashSelector<>());
        for (int i = 0; i < THREAD_NUM; i++) {
            battleStation.put(new Bus<>(i, "battle"));
        }
        StationDriver battleDriver = new StationDriver(battleStation, INITIAL_DELAY, TICK_PERIOD);
        battleDriver.runStation();
        BattleService.getService().init(battleStation);
        logger.info("Battle Service initialized successfully.");
    }

    private static void initializeWebSocketService() {
        logger.info("Initializing WebSocket Service with THREAD_NUM: {} and DEFAULT_PORT: {}...", THREAD_NUM, WebSocketService.DEFAULT_PORT);
        WebSocketService net = new WebSocketService(WebSocketService.DEFAULT_PORT, THREAD_NUM, MessageDispatcher.getInstance(), new MessageCoder());
        new Thread(() -> {
            try {
                net.start();
            } catch (InterruptedException e) {
                logger.error("WebSocket Service failed to start.", e);
                throw new RuntimeException(e);
            }
        }).start();
        logger.info("WebSocket Service initialized successfully.");
    }
}