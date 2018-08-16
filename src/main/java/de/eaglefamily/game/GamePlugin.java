/**
 * Created by _BlackEagle_ on 24.07.2018 11:08:45
 */
package de.eaglefamily.game;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.eaglefamily.bukkitlibrary.language.LanguageManager;
import de.eaglefamily.game.listener.GameStartEndListener;

/**
 * @author _BlackEagle_
 */
public abstract class GamePlugin extends JavaPlugin {

	protected final Game game = new Game(this);
	protected Configuration config;
	private GameStartEndListener gameListener;

	protected void loadConfig() {
		if (!getDataFolder().exists()) getDataFolder().mkdir();
		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			try (InputStream in = getResource("config.yml")) {
				Files.copy(in, configFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	protected void loadLanguages(String path) {
		File languageFolder;
		if (path == null) languageFolder = new File(getDataFolder(), "language");
		else languageFolder = new File(path);
		// load languages when language folder doesn't exists
		if (!languageFolder.exists()) {
			languageFolder.mkdir();
			File de_DE = new File(languageFolder, "de_DE.yml");
			try (InputStream in = getResource("language/de_DE.yml")) {
				Files.copy(in, de_DE.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Game.getInstance().getConfig().getStringList("language.all").forEach(language -> {
			File languageFile = new File(languageFolder, language + ".yml");
			if (languageFile.exists()) LanguageManager.loadLanguageFromConfig(language,
					YamlConfiguration.loadConfiguration(languageFile));
		});
	}

	protected void loadLanguages() {
		loadLanguages(null);
	}

	protected void registerGameListener(Listener listener) {
		if (gameListener == null) {
			gameListener = new GameStartEndListener();
			Bukkit.getPluginManager().registerEvents(gameListener, this);
		}
		gameListener.addListener(listener);
	}
}
