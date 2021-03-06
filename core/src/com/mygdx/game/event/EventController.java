package com.mygdx.game.event;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.event.eventHelpers.Distance;
import com.mygdx.game.event.events.IEvent;
import com.mygdx.game.event.events.TonnelEvent;
import com.mygdx.game.models.player.APlayer;
import com.mygdx.game.process.GameProcess;

import java.util.Vector;

/**
 * Created by artem on 10/10/17.
 */

public class EventController {
    private Vector<IEvent> freeEvents;
    private Vector<IEvent> playerEvents;

    public EventController() {
        freeEvents = new Vector<IEvent>();
        playerEvents = new Vector<IEvent>();
    }

    public void addEvent(IEvent event) {
        freeEvents.add(event);
    }

    public void act(SpriteBatch batch) {
        this.addEventToPlayers();
        for (int i = playerEvents.size() - 1; i >= 0; i--) {
            playerEvents.get(i).act(batch);
            if (playerEvents.get(i).isFinish()) {
                playerEvents.get(i).dispose();
                playerEvents.remove(i);
            }
        }
    }

    public Vector<IEvent> getAllEvents() {
        Vector<IEvent> all = new Vector<IEvent>();
        all.addAll(freeEvents);
        all.addAll(playerEvents);
        return all;
    }

    private void addEventToPlayers() {
        if (freeEvents.size() > 0) {
            IEvent event = freeEvents.get(0);
            Vector<APlayer> freePlayers = new Vector<APlayer>();
            for (int i = 0; i < GameProcess.playerMap.getAll().size(); i++) {
                APlayer player = GameProcess.playerMap.getAll().get(i);
                if (freeEvents.size() > 0 && !player.isBusy()) {
                    freePlayers.add(player);
                }
            }
            if (freePlayers.size() == 0) {
                return;
            }
            if (freePlayers.size() == 1) {
                event.setPlayer(freePlayers.get(0));
                playerEvents.add(event);
                freeEvents.remove(0);
            } else {
                int min = 100000;
                APlayer player = null;
                for (int i = 0; i < freePlayers.size(); i ++) {
                    int distance = Distance.getDistance(freePlayers.get(i).actualPosition, event.getCellForDistance());
                    if (distance < min) {
                        min = distance;
                        player = freePlayers.get(i);
                    }
                }
                event.setPlayer(player);
                playerEvents.add(event);
                freeEvents.remove(0);
            }
        }
    }

    public void dispose() {

    }
}
