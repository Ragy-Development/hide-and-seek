package cz.ragy.hideandseek.managers;

import com.sun.org.apache.xerces.internal.xs.StringList;
import cz.ragy.hideandseek.game.Arena;
import cz.ragy.hideandseek.HideAndSeek;
import cz.ragy.hideandseek.utilities.Colors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    public static File configFile;
    public static File arenasFile;
    public static YamlConfiguration arenas;

    public static YamlConfiguration config;
    public static String setArena;
    public static String creating;
    public static String arenaCreated;
    public static String arenaExists;
    public void startup() {
        configFile = new File(HideAndSeek.instance.getDataFolder(), "config.yml");
        arenasFile = new File(HideAndSeek.instance.getDataFolder(), "arenas.yml");
        if(!configFile.exists()) {
            HideAndSeek.instance.saveResource("config.yml", true);
        }
        if(!arenasFile.exists()) {
            HideAndSeek.instance.saveResource("arenas.yml", true);
        }
        arenas = new YamlConfiguration().loadConfiguration(arenasFile);
        config = new YamlConfiguration().loadConfiguration(configFile);
        ArenaManager.STATICARENAS = new ArrayList<>();
        new ArenaManager().loadArenas();
        creating = config.getString("Create-Arena.Creating-Arena");
        arenaCreated = config.getString("Create-Arena.Created");
        arenaExists = config.getString("Create-Arena.Arena-Exists");
    }
    public void reload() {
        if(!configFile.exists()) {
            HideAndSeek.instance.saveResource("config.yml", true);
        }
        if(!arenasFile.exists()){
            HideAndSeek.instance.saveResource("arenas.yml", true);
        }
        configFile = new File(HideAndSeek.instance.getDataFolder(), "config.yml");
        arenasFile = new File(HideAndSeek.instance.getDataFolder(), "arenas.yml");
        arenas = YamlConfiguration.loadConfiguration(arenasFile);
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public void saveArenasToConfig(List<Arena> arenaList, CommandSender sender){
        ConfigurationSection parentSection = arenas.getConfigurationSection("arenas");

        for (Arena arena : arenaList) {
            if (parentSection.getConfigurationSection(arena.arenaName) == null) {
                ConfigurationSection childSection = parentSection.createSection(arena.arenaName);
                setArena = arenaCreated;
                setArena = setArena.replace("%arena%", arena.arenaName);
                childSection.set("ArenaWorld", arena.arenaWorldName);
                childSection.set("ArenaMaxPlayers", arena.maxPlayers);
                childSection.set("ArenaMinPlayers", arena.minPlayers);
                childSection.set("ArenaSeekersCount", arena.seekersCount);
                childSection.set("LobbyLocation", arena.lobbyLocation);
                childSection.set("SeekersLocation", arena.seekersLocation);
                childSection.set("HidersLocation", arena.hidersLocation);
                for(int i = 0; i > 10; i++){
                    sender.sendMessage("");
                }
                sender.sendMessage(Colors.translate("&7&m----------------"));
                sender.sendMessage(Colors.translate("&cArena: " + arena.arenaName));
                sender.sendMessage(Colors.translate("&cWorld: " + arena.arenaWorldName));
                sender.sendMessage(Colors.translate("&cMax Players: " + arena.maxPlayers));
                sender.sendMessage(Colors.translate("&cMin Players: " + arena.minPlayers));
                sender.sendMessage(Colors.translate("&cSeekers: " + arena.seekersCount));
                sender.sendMessage("");
                sender.sendMessage(Colors.translate(creating));
                sender.sendMessage(Colors.translate(setArena));
                sender.sendMessage(Colors.translate("&7&m----------------"));
            } else {
                sender.sendMessage(Colors.translate(arenaExists));
                break;
            }
        }
        try {
            arenas.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editArena(String arenaName, String whatToEdit, String toWhat) {
        ConfigurationSection parentSection = arenas.getConfigurationSection("arenas");
        for (String key : ConfigManager.arenas.getConfigurationSection("arenas").getKeys(false)) {
            if(key != arenaName) {
                System.out.println("err");
                break;
            } else {
                ConfigurationSection childSection = parentSection.getConfigurationSection(arenaName);
                childSection.set(whatToEdit, toWhat);
            }
        }
    }
    public void editArenaName(String arenaName, String arenaNewName) {
        ConfigurationSection parentSection = arenas.getConfigurationSection("arenas");
        ConfigurationSection oldSection = config.getConfigurationSection(arenaName);

        if (oldSection == null) {
            return;
        }
        Map<String, Object> values = oldSection.getValues(true);
        parentSection.set(arenaNewName, null);
        parentSection.createSection(arenaNewName, values);
    }
    public void reloadAllConfigs() {
        Bukkit.getPluginManager().disablePlugin(HideAndSeek.instance);
        Bukkit.getPluginManager().enablePlugin(HideAndSeek.instance);
    }
    public boolean arenaExists(String arenaName) {
        ConfigurationSection parentSection = arenas.getConfigurationSection("arenas");
        if (parentSection.getConfigurationSection(arenaName) == null) {
            return false;
        } else {
            return true;
        }
    }
    public String getStringFromConfig(String string) {
        return config.getString(string);
    }
    public void reloadConfigs() {
        File arenaFile = new File(HideAndSeek.instance.getDataFolder(), "arenas.yml");
        arenas = YamlConfiguration.loadConfiguration(arenaFile);
        File cfgFile = new File(HideAndSeek.instance.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(cfgFile);
    }
}
