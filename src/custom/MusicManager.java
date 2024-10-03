package custom;

import javax.sound.sampled.Clip;

import blib.util.Audio;
import blib.util.BTools;
import ent.Background;
import ent.HUD;
import ent.game.boss.Boss;
import trident.Trident;
public class MusicManager {

    private static Song[] titleSongs = {
        new Song("data/music/abre.wav", "Abre - Blocky"),
        new Song("data/music/blades.wav", "Blades - Blocky"),
    };
    private static Song[] songs = {
        new Song("data/music/slimes lament.wav", "Slime's Lament - Blocky"),
        new Song("data/music/poaceae.wav", "Poaceae - Blocky"),
        new Song("data/music/puerta.wav", "Puerta - Blocky"),
    };
    private static Song[] caveSongs = {
        new Song("data/music/cavesong1.wav", "Caves and Crystals - Blocky"),
        new Song("data/music/duet in the depths.wav", "Duet in the Depths - Blocky"),
    };
    private static Song[] bossSongs = {
        new Song("data/music/slimes court.wav", "Slime's Court - Blocky"), // Apex Slime
        new Song("data/music/moleSong.wav", "molesong - rascal writes"), // Big Mole (The Supervisor)
    };

    public static double volume = 1;
    public static Clip currentSong = null;
    public static long songTime = 1000;
    public static String lastName = "NO SONG";
    
    private static boolean boss = false;
    private static int bossMusic;

    public static void update(long elapsedTime){
        if(songTime > 0) songTime -= elapsedTime;

        if(Trident.getCurrentScene().name.equals("newTut")){
            stopSong();
        }

        if((currentSong == null || !currentSong.isActive()) && !Trident.getCurrentScene().name.equals("newTut")){
            if(boss) songTime = 0;
            if(songTime <= 0){
                // Play new song
                songTime = 180000; // 3 minutes
                if(boss){
                    currentSong = bossSongs[bossMusic].play();
                    lastName = bossSongs[bossMusic].name;
                }else{
                    if(Trident.getCurrentScene().name.equals("title")){
                        int songGroup = BTools.randInt(0, 3);
                        Song[] songArr = songs;
                        if(songGroup == 1) songArr = caveSongs;
                        if(songGroup == 2) songArr = titleSongs;

                        int selSong = BTools.randInt(0, songArr.length);
                        currentSong = songArr[selSong].play();
                        lastName = songArr[selSong].name;
                    }else{
                        if(Background.bg == Background.SURFACE){
                            int selSong = BTools.randInt(0, songs.length);
                            currentSong = songs[selSong].play();
                            lastName = songs[selSong].name;
                        }else if(Background.bg == Background.MINES || Background.bg == Background.DEEPMINES){
                            int selSong = BTools.randInt(0, caveSongs.length);
                            currentSong = caveSongs[selSong].play();
                            lastName = caveSongs[selSong].name;
                        }

                        HUD.addNotif(lastName, HUD.NOTIF_MUSIC);
                    }
                    
                }
            }
        }
        if(boss && !Boss.hasBoss()){
            stopBoss();
        }
    }

    public static void sceneChange(String scene){
        stopSong();
        if(scene.equals("world")) songTime = 5000;
        else songTime = 1000;
        boss = false;
    }

    public static void stopSong(){
        lastName = "NO SONG";
        if(currentSong != null){
            currentSong.stop();
            currentSong = null;
        }
    }

    public static void startBossMusic(int b){
        boss = true;
        bossMusic = b;
        stopSong();
        songTime = 0;
    }

    public static void stopBoss(){
        boss = false;
        stopSong();
        songTime = 10000;
    }



    private static class Song {
        public String filePath;
        public String name;

        public Song(String file, String n){
            filePath = file;
            name = n;
        }

        public Clip play(){
            return Audio.play(filePath, volume);
        }   
    }
}
