package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.core.task.Task;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WPlayer {
    private final UUID uuid;
    private String colorName;
    private Role role;

    private Boolean killing = false;
    private Boolean died = false;
    private Boolean report = false;
    private Boolean voted = false;


    public WPlayer(UUID uuid) {
        this(uuid, Role.CLUE_MATE);
    }

    public WPlayer(UUID uuid, Role role) {
        this.uuid = uuid;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setColor(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    public Boolean isKilling() {
        return killing;
    }

    public void setKilling(Boolean killing) {
        this.killing = killing;
    }

    public Boolean isDied() {
        return died;
    }

    public void setDied(Boolean died) {
        this.died = died;
    }

    public Boolean getReport() {
        return report;
    }

    public void setReport(Boolean report) {
        this.report = report;
    }

    public Boolean wasVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }
}
