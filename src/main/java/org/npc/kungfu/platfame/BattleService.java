package org.npc.kungfu.platfame;

import org.npc.kungfu.logic.BattleRingManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BattleService {

    BattleRingManager battleRingManager;

    public BattleService(ITaskStation station) {

    }

    public void init(){
        ExecutorService service = Executors.newFixedThreadPool(2,new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        });

//        service.submit();
    }

}
