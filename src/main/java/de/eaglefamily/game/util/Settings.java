/*
 * Created by Jan on 24.07.2018 14:45:19
 */
package de.eaglefamily.game.util;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

/**
 * The Class Settings.
 *
 * @author Jan
 */
public class Settings {

	/** The global config. */
	public static boolean globalConfig = false;
	
	/** The config path. */
	public static String configPath;
	
	/** The global languages. */
	public static boolean globalLanguages = false;
	
	/** The language path. */
	public static String languagePath;
	
	/** The global maps. */
	public static boolean globalMaps = false;
	
	/** The maps path. */
	public static String mapsPath;

	/** The vote map. */
	public static boolean voteMap = true;
	
	/** The voteable maps. */
	public static int voteableMaps = 3;
	
	/** The map. */
	public static String map;

	/** The teams. */
	public static boolean teams = true;
	
	/** The number of teams. */
	public static int numberOfTeams = 2;
	
	/** The number of players per team. */
	public static int numberOfPlayersPerTeam = 1;

	/** The lobby max players. */
	public static int lobbyMaxPlayers = numberOfTeams * numberOfPlayersPerTeam;
	
	/** The game max players. */
	public static int gameMaxPlayers = lobbyMaxPlayers * 2;

	/** The spectators. */
	public static boolean spectators = true;

	/** The lobby short start enabled. */
	public static boolean lobbyShortStartEnabled = true;
	
	/** The player for lobby start. */
	public static int playerForLobbyStart = numberOfPlayersPerTeam + 1;
	
	/** The players for lobby short start. */
	public static int playersForLobbyShortStart = numberOfTeams * numberOfPlayersPerTeam;
	
	/** The lobby countdown. */
	public static int lobbyCountdown = 60;
	
	/** The lobby short start. */
	public static int lobbyShortStart = 10;

	/** The friendly fire. */
	public static boolean friendlyFire = false;
	
	/** The game contents. */
	public static boolean gameContents = false;
	
	/** The spawn. */
	public static boolean spawn = true;

	/** The actionbar team. */
	public static boolean actionbarTeam = false;

	/** The undecided. */
	public static boolean undecided = true;

	/** The end countdown. */
	public static int endCountdown = 21;

	/** The stats. */
	public static boolean stats = true;
	
	/** The stats scored. */
	public static boolean statsScored = true;
	
	/** The stats parameter. */
	public static List<StatsParameter> statsParameter = Lists.newArrayList();
	
	/** The stats replaces. */
	public static Consumer<StatsReplaces> statsReplaces = statsReplaces -> statsReplaces.accept(Lists.newArrayList());
	
	/** The round stats replaces. */
	public static Consumer<RoundStatsReplaces> roundStatsReplaces = statsReplaces -> statsReplaces
			.accept(Lists.newArrayList());
	
	/** The rejoin. */
	public static boolean rejoin = false;

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public static String getSize() {
		return Settings.numberOfTeams + "x" + Settings.numberOfPlayersPerTeam;
	}
}
