package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.core.task.Task;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WPlayer {
    private final UUID uuid;
    private UUID votedPlayerUUID;
    private Color color;
    private Role role;
    private List<Task> tasks = new ArrayList<>();

    private Boolean killing = false;
    private Boolean died = false;
    private Boolean report = false;


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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void clearTasks() {
        tasks.clear();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public UUID getVotedPlayerUUID() {
        return votedPlayerUUID;
    }

    public void setVotedPlayerUUID(UUID votedPlayerUUID) {
        this.votedPlayerUUID = votedPlayerUUID;
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
}
