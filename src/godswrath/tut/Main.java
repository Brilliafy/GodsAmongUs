package godswrath.tut;

import godswrath.tut.commands.CommandInvokeGods;
import godswrath.tut.gods.*;
import godswrath.tut.listeners.*;
import io.netty.util.internal.ThreadLocalRandom;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main extends JavaPlugin {

public static double GodFortuneChancePercent = 1.5625;
public static double GodNewQuestChancePercent = 35;
public static int GodEventDurationTicks = 1200;
public static int GodEventIntervalTicks = 3600;
public static int GodNewQuestIntervalTicks = 3800;
public static int questDelay = 200;
    public static List<Player> playersQuestCompleted = new ArrayList<Player>();

public static List<String> EventMessages = new ArrayList<String>(Arrays.asList(
        "&o&7The Gods soundlessly roam amongst us...",
        "&7&oYou sense someone watching you...",
        "&7&oThe gods are conversing in Olympus about you...",
        "&6&lKoalemos&r &7&oobnoxiously suggested you were dealt with by the other Gods...",
        "&e&lDike&r &o&7requests justice in your name by the other Gods.",
        "&o&7You see a spirit flying down to you from the Horizon...",
        "&o&7The Gods felt disturbed with your recent behaviour...",
        "&o&7The Gods doubt you believe in them...",
        "&o&7A loud mystic voice behind you summons you to be taken care of...",
        "&e&lSoteria&r &o&7grabs your hand by the back, and pulls you backwards, asking for damnation by the other Gods."));


    @Override
    public void onEnable() {
        new OnAttackListener(this);
        new OnBreakListener(this);
        new OnMovementListener(this);
        new OnEntityDeathListener(this);
        new OnPlayerDeathListener(this);
        new OnHarvestListener(this);
        new OnBedEnter(this);
        new OnAnimalsBreed(this);
        new OnInventoryClickListener(this);
        new OnConsumeListener(this);
        new OnPlayerDropListener(this);
        new OnProjectileListener(this);
        new OnPlayerDropListener(this);
        new OnPlaceListener(this);

        Objects.requireNonNull(this.getCommand("invokegods")).setExecutor(new CommandInvokeGods());

        Bukkit.getConsoleSender().sendMessage("The Gods have been summoned upon this world.");

        getServer().getScheduler().runTaskTimer(this, Main::invokeGodsAllPlayers, GodEventDurationTicks, GodEventIntervalTicks);
        getServer().getScheduler().runTaskTimer(this, Main::invokeGodsQuestsAllPlayers, 0, GodNewQuestIntervalTicks);
    }

    public static void invokeGodsAllPlayers() {
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', EventMessages.get(ThreadLocalRandom.current().nextInt(0, EventMessages.size()))));
        for(Player player : Bukkit.getOnlinePlayers()) {
            requestGodFortune(player);
        }
    }

    public static void invokeGodsQuestsAllPlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            invokeGodsQuests(player);
            Bukkit.getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> requestNewQuest(player), questDelay);
        }
    }

    public static void requestGodFortune(Player player) {
        for (int godIndex = 9; godIndex >= 0; godIndex--) {
            if (ThreadLocalRandom.current().nextDouble(0, 100) <= GodFortuneChancePercent) {
                switch (godIndex) {
                    case 0 -> new Zeus().Invoke(player);
                    case 1 -> new Hades().Invoke(player);
                    case 2 -> new Tyche().Invoke(player);
                    case 3 -> new Hephaestus().Invoke(player);
                    case 4 -> new Neptune().Invoke(player);
                    case 5 -> new Hermes().Invoke(player);
                    case 6 -> new Ares().Invoke(player);
                    case 7 -> new Apollo().Invoke(player);
                    case 8 -> new Demeter().Invoke(player);
                    case 9 -> new Erebus().Invoke(player);
                    //add particles on attacks and stuff see google?

                    //add objectives to each god to please him or not and get a reward
                    //revert GodDuration values
                    //fix warnings bad coding practices
                    //configs or and commands
                }
            }
        }
    }

    public static void requestNewQuest(Player player) {
        if (ThreadLocalRandom.current().nextDouble(0, 100) <= GodNewQuestChancePercent) {
            int godIndex = ThreadLocalRandom.current().nextInt(0,11 + 1);
            switch (godIndex) {
                case 0 -> new Zeus().requestQuest(player);
                case 1 -> new Hades().requestQuest(player);
                case 2 -> new Tyche().requestQuest(player);
                case 3 -> new Hephaestus().requestQuest(player);
                case 4 -> new Neptune().requestQuest(player);
                case 5 -> new Hermes().requestQuest(player);
                case 6 -> new Ares().requestQuest(player);
                case 7 -> new Apollo().requestQuest(player);
                case 8 -> new Demeter().requestQuest(player);
                case 9 -> new Erebus().requestQuest(player);
                case 10 -> new Atropos().requestQuest(player);
                case 11 -> new Nemesis().requestQuest(player);
            }
        }
    }

    public static void invokeGodsQuests(Player player) {
        if (Apollo.QuestPlayerList.contains(player)) {
            new Apollo().invokeQuest(player,false);
        }
        if (Ares.QuestPlayerList.contains(player)) {
            new Ares().invokeQuest(player,false);
        }
        if (Atropos.QuestPlayerList.contains(player)) {
            new Atropos().invokeQuest(player,false);
        }
        if (Demeter.QuestPlayerList.contains(player)) {
            new Demeter().invokeQuest(player,false);
        }
        if (Erebus.QuestPlayerList.contains(player)) {
            new Erebus().invokeQuest(player,false);
        }
        if (Hades.QuestPlayerList.contains(player)) {
            new Hades().invokeQuest(player,false);
        }
        if (Hephaestus.QuestPlayerList.contains(player)) {
            new Hephaestus().invokeQuest(player,false);
        }
        if (Hermes.QuestPlayerList.contains(player)) {
            new Hermes().invokeQuest(player,false);
        }
        if (Nemesis.QuestPlayerList.contains(player)) {
            new Nemesis().invokeQuest(player,false);
        }
        if (Neptune.QuestPlayerList.contains(player)) {
            new Neptune().invokeQuest(player,false);
        }
        if (Tyche.QuestPlayerList.contains(player)) {
            new Tyche().invokeQuest(player,false);
        }
        if (Zeus.QuestPlayerList.contains(player)) {
            new Zeus().invokeQuest(player,false);
        }
    }

    public static void requestQuestLabel(Player player, String message) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 500.0f, 1.0f);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&a&2&nNew Quest!"),
                ChatColor.translateAlternateColorCodes('&', message),
                20,80,20);
        toggleQuestIndicator(message,player);
    }

    public static void invokeQuest(Player player, Boolean completed, Runnable completeConsequence, Runnable failedConsequence, String completedMessage, String failedMessage) {
        if(completed) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 500.0f, 1.0f);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&l&2&nQuest completed!"),
                    ChatColor.translateAlternateColorCodes('&', completedMessage),
                    20,80,20);
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColor.translateAlternateColorCodes('&', completedMessage)));
            completeConsequence.run();
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 500.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 500.0f, 1.0f);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&4&l&nQuest Failed!"),
                    ChatColor.translateAlternateColorCodes('&', failedMessage),
                    20,40,20);
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColor.translateAlternateColorCodes('&', failedMessage)));
            failedConsequence.run();
        }
        playersQuestCompleted.add(player);
    }

    public static void toggleQuestIndicator(String content, Player player) {
         new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                if(time == (GodNewQuestIntervalTicks - questDelay) / 20 || playersQuestCompleted.contains(player)){
                    if(playersQuestCompleted.contains(player)) {
                        playersQuestCompleted.remove(player);
                    }
                    cancel();
                }else{
                    time++;
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent(ChatColor.translateAlternateColorCodes('&', content + String.format("&l&f(&0%s&f/&0%s&f)",time,(GodNewQuestIntervalTicks - questDelay) / 20))));
                }

            }
        }.runTaskTimer(getPlugin(Main.class),0,20);
    }


    //make 2x to invoke underworld gods when in nether/end
//        public int isPlayedInNetherBonus(Player player){
//            return (player.getWorld().getEnvironment().equals(World.Environment.NETHER) ? 1 : 0) + 1;
//        }

    }


