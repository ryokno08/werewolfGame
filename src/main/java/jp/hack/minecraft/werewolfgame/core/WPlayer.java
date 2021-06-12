package jp.hack.minecraft.werewolfgame.core;

import java.util.UUID;

public class WPlayer {
    private final UUID uuid;
    private String colorName;

    private Boolean canMove = true;
    private Boolean wasDied = false;
    private Boolean wasReported = false;
    private Boolean wasVoted = false;


    public WPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public WPlayer(WPlayer wPlayer) {
        this(wPlayer.getUuid());

        setColorName(wPlayer.getColorName());
        setWasDied(wPlayer.wasDied());
        setWasReported(wPlayer.wasReported());
        setWasVoted(wPlayer.wasVoted());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    public Boolean canMove() {
        return canMove;
    }

    public void setCanMove(Boolean canMove) {
        this.canMove = canMove;
    }

    public Boolean wasDied() {
        return wasDied;
    }

    public void setWasDied(Boolean wasDied) {
        this.wasDied = wasDied;
    }

    public Boolean wasReported() {
        return wasReported;
    }

    public void setWasReported(Boolean wasReported) {
        this.wasReported = wasReported;
    }

    public Boolean wasVoted() {
        return wasVoted;
    }

    public void setWasVoted(Boolean wasVoted) {
        this.wasVoted = wasVoted;
    }

    public Boolean isImposter() {
        return (this instanceof Imposter);
    }
}
