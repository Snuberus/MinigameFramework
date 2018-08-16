/**
 * Created by _BlackEagle_ on 24.07.2018 14:45:19
 */
package de.eaglefamily.game.util;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

/**
 * @author _BlackEagle_
 */
public class Settings {

	public static boolean globalConfig = false;
	public static String configPath;
	public static boolean globalLanguages = false;
	public static String languagePath;
	public static boolean globalMaps = false;
	public static String mapsPath;

	public static boolean voteMap = true;
	public static int voteableMaps = 3;
	public static String map;

	public static boolean teams = true;
	public static int numberOfTeams = 2;
	public static int numberOfPlayersPerTeam = 1;

	public static int lobbyMaxPlayers = numberOfTeams * numberOfPlayersPerTeam;
	public static int gameMaxPlayers = lobbyMaxPlayers * 2;

	public static boolean spectators = true;

	public static boolean lobbyShortStartEnabled = true;
	public static int playerForLobbyStart = numberOfPlayersPerTeam + 1;
	public static int playersForLobbyShortStart = numberOfTeams * numberOfPlayersPerTeam;
	public static int lobbyCountdown = 60;
	public static int lobbyShortStart = 10;

	public static boolean friendlyFire = false;
	public static boolean gameContents = false;
	public static boolean spawn = true;

	public static boolean actionbarTeam = false;

	public static boolean undecided = true;

	public static int endCountdown = 21;

	public static boolean stats = true;
	public static boolean statsScored = true;
	public static List<StatsParameter> statsParameter = Lists.newArrayList();
	public static Consumer<StatsReplaces> statsReplaces = statsReplaces -> statsReplaces.accept(Lists.newArrayList());
	public static Consumer<RoundStatsReplaces> roundStatsReplaces = statsReplaces -> statsReplaces
			.accept(Lists.newArrayList());

	public static String getSize() {
		return Settings.numberOfTeams + "x" + Settings.numberOfPlayersPerTeam;
	}
}
