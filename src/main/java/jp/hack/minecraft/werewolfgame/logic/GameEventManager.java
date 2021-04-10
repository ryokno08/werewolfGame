package jp.hack.minecraft.werewolfgame.logic;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.gamerule.PlayerKill;
import jp.hack.minecraft.werewolfgame.core.state.PlayingState;
import jp.hack.minecraft.werewolfgame.core.state.VotingState;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameEventManager implements Listener {
    private JavaPlugin plugin;
    private Game game;

    public GameEventManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.game = ((GameConfigurator) this.plugin).getGame();

        if (game == null) return;
        if (!game.wasStarted()) return;

        Player p = event.getPlayer();
        WPlayer wPlayer = game.getWPlayer(p.getUniqueId());

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            // デフォルト値の設定
            // if(lobbyLocation == null)  lobbyLocation = new Location(player.getWorld(),182,5,-134);
            if (game.getLobbyPos() != null) p.teleport(game.getLobbyPos());

            if (wPlayer.isDied()) {
                game.getDisplayManager().invisible(p);
                game.getDisplayManager().showDeath(p, "because of you");
            }
        }, 1);
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();

        if (game == null) return;
        if (!game.wasStarted()) return;

        Player p = e.getPlayer();

        if (game.canSpeak()) return; //　現在喋ることが許されているか取得し判断する。許されていたらreturn

        e.setCancelled(true);
        p.sendMessage(ChatColor.RED + "現在、チャットで会話することはできません。");

    }

    @EventHandler
    public void onPlayerPickItem(EntityPickupItemEvent event) {
        this.game = ((GameConfigurator) this.plugin).getGame();

        if (game == null) return;
        if (!game.wasStarted()) return;
        if (!(event.getEntity() instanceof Player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        this.game = ((GameConfigurator) this.plugin).getGame();

        if (game == null) return;
        if (!game.wasStarted()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();

        if (game == null) return;
        if (!game.wasStarted()) return;

        if (game.getWPlayer(e.getPlayer().getUniqueId()).isDied()) return;

        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())
            return;
        if (!game.canMove()) e.setCancelled(true);
        if (game.getWPlayer(e.getPlayer().getUniqueId()).isKilling()) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.game = ((GameConfigurator) this.plugin).getGame();

        if (game == null) return;
        if (!game.wasStarted()) return;

        Player player = event.getPlayer();
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
        wPlayer.setDied(true);

        game.confirmGame();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);

        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        if (!(game.getCurrentState() instanceof PlayingState)) return;

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player entity = (Player) e.getEntity();

            WPlayer wAttacker = game.getWPlayer(attacker.getUniqueId());
            WPlayer wEntity = game.getWPlayer(entity.getUniqueId());
            if (wAttacker.isDied()) {
                return;
            }
            if (wEntity.isDied()) {
                game.getDisplayManager().sendMessage(entity, "you.disturbImposter");
            }

            if (Role.CLUE_MATE.equals(game.getPlayerRole(entity.getUniqueId()))) {
                if (Role.IMPOSTER.equals(game.getPlayerRole(attacker.getUniqueId()))) {
                    if (attacker.getInventory().getItemInMainHand().getType().equals(game.getItemForKill().getType())) {

                        System.out.println("[!KILL]" + attacker.getDisplayName() + " killed " + entity.getDisplayName());
                        new PlayerKill(plugin).onPlayerAttack(e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemAction(PlayerInteractEvent event) {
        this.game = ((GameConfigurator) this.plugin).getGame();

        if (game == null) return;
        if (!game.wasStarted()) return;
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        if (game.getCurrentState() instanceof VotingState) {
            if (item.getType() == game.getItemForVote().getType()) {
                if (!item.getItemMeta().getDisplayName().equals(game.getItemForVote().getItemMeta().getDisplayName())) {
                    return;
                }
                Gui voteGui = new Gui(3, "投票先を選んでください");
                List<GuiItem> heads = new ArrayList<>();
                game.getWPlayers().values().stream()
                        .filter(wPlayer -> !wPlayer.isDied())
                        .map(wPlayer -> plugin.getServer().getPlayer(wPlayer.getUuid()))
                        .forEach(headPlayer -> {
                            UUID uuid = headPlayer.getUniqueId();

                            ItemStack skullStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                            SkullMeta skull = (SkullMeta) skullStack.getItemMeta();
                            skull.setDisplayName(headPlayer.getName());
                            skull.setOwningPlayer(headPlayer);
                            skullStack.setItemMeta(skull);

                            ItemBuilder skullBuilder = ItemBuilder.from(skullStack);
                            heads.add(
                                    skullBuilder.asGuiItem(e -> {
                                        e.setCancelled(true);
                                        game.voteToPlayer(e.getWhoClicked().getUniqueId(), uuid);
                                    })
                            );
                        });
                plugin.getLogger().info(String.valueOf(heads.size()));
                heads.forEach(i -> voteGui.addItem(i));

                ItemStack skipItem = new ItemStack(Material.BARRIER);
                ItemMeta meta = skipItem.getItemMeta();
                meta.setDisplayName("Skip");
                skipItem.setItemMeta(meta);
                ItemBuilder skipBuilder = ItemBuilder.from(skipItem);
                voteGui.setItem(26, skipBuilder.asGuiItem(e -> {
                    e.setCancelled(true);
                    game.voteToSkip(e.getWhoClicked().getUniqueId());
                }));
                voteGui.open(player);
                event.setCancelled(true);
            }
        }

        if (!(game.getCurrentState() instanceof PlayingState)) return;

        if (item.getType() == game.getItemForReport().getType()) {

            Location playerLoc = player.getLocation();

            game.getCadavers().values().forEach(cadaver -> {
                Location cadaverLoc = cadaver.getCadaverBlock().getLocation();
                if (cadaverLoc.distance(playerLoc) <= game.getReportDistance()) {
                    game.report(player, cadaver.getPlayer());
                }
            });
        }
    }

}