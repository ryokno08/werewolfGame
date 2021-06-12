package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Imposter extends WPlayer {
    private Boolean canKill = false;
    private BukkitTask coolDown;
    int count = 0;

    public Imposter(WPlayer wPlayer) {
        super(wPlayer.getUuid());

        this.setWasDied(wPlayer.wasDied());
        this.setWasReported(wPlayer.wasReported());
        this.setWasVoted(wPlayer.wasVoted());
        this.setColorName(wPlayer.getColorName());
    }

    public Imposter(UUID uuid) {
        super(uuid);
    }

    public Boolean canKill() {
        return canKill;
    }

    public void setCanKill(Boolean canKill) {
        this.canKill = canKill;
    }

    public void setCoolDown(Game game) {
        if (coolDown != null) {
            if (!coolDown.isCancelled()) {
                coolDown.cancel();
            }
        }
        count = 0;
        this.coolDown = new CoolDown(game).runTaskLater(game.getPlugin(), 20);
    }

    public void clearCoolDown() {
        coolDown.cancel();
    }

    public class CoolDown extends BukkitRunnable {
        private final Game game;
        private final int coolTime;

        public CoolDown(Game game) {
            this.game = game;
            this.coolTime = game.getCoolTimeOfKilling();
            canKill = false;
        }

        @Override
        public void run() {
            if (coolTime <= count) {
                canKill = true;
                game.getDisplayManager().sendActionBarMessage(game.getPlayer(getUuid()), "");
                this.cancel();
            } else {
                game.getDisplayManager().sendActionBarMessage(game.getPlayer(getUuid()), "キルクールタイム " + ChatColor.RED.toString() + (coolTime - count) + ChatColor.RESET.toString() + " 秒");
                count++;
                coolDown = new CoolDown(game).runTaskLater(game.getPlugin(), 20);
            }
        }

        public int getCount() {
            return count;
        }
    }
}
