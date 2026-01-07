package de.husten1997.copyinstance;

public final class RelevantFolder {
    private RelevantFolder() {}

    public static final String SAVE = "saves";
    public static final String BACKUP = "backups";
    public static final String JOURNEYMAP = "journeymap";
    public static final String VISUALPROSPECTING = "visualprospecting";
    public static final String TC_NODE_TRACKER = "TCNodeTracker";
    public static final String SCHEMATICS = "schematics";
    public static final String RESOURCEPACKS = "resourcepacks";
    public static final String SHADERPACKS = "shaderpacks";
    public static final String SCREENSHOTS = "screenshots";
    public static final String LOCALCONFIG = "localconfig.cfg";
    public static final String BOTANIA_VARS = "BotaniaVars.dat";
    public static final String OPTIONS = "options.txt";
    public static final String OPTIONSNF = "optionsnf.txt";
    public static final String SERVERS = "servers.dat";

    public static final String[] RELEVANT_FOLDERS = new String[] {SAVE, BACKUP, JOURNEYMAP, VISUALPROSPECTING, TC_NODE_TRACKER, SCHEMATICS, RESOURCEPACKS, SHADERPACKS, SCREENSHOTS};
    public static final String[] RELEVANT_FILES = new String[] {LOCALCONFIG, BOTANIA_VARS, OPTIONS, OPTIONSNF, SERVERS};
}
