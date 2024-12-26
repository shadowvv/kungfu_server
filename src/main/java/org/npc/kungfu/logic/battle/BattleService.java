package org.npc.kungfu.logic.battle;

import org.npc.kungfu.platfame.TaskStation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BattleService {

    private static final BattleService service = new BattleService();

    public static BattleService getService() {
        return service;
    }

    BattleRingManager battleRingManager;

    public BattleService() {

    }

    public void init(TaskStation battleStation){
        ExecutorService service = Executors.newFixedThreadPool(2,new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        });

//        service.submit();
    }

}