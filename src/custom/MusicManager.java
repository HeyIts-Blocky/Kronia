package custom;

import javax.sound.sampled.Clip;

import blib.util.*;
import ent.game.boss.*;
import trident.Trident;
import ent.*;
public class MusicManager {

    private static Song[] songs = {
        new Song("data/music/A World is Discovered.wav", "A World is Discovered - Suno AI"),
        new Song("data/music/A World Lies Dormant.wav", "A World Lies Dormant - Suno AI"),
        new Song("data/music/A World Waits Undiscovered.wav", "A World Waits Undiscovered - Suno AI"),
        new Song("data/music/Rats on Mars.wav", "Rats on Mars - Suno AI"),
    };
    private static Song[] caveSongs = {
        new Song("data/music/cavesong1.wav", "Caves and Crystals - Blocky"),
    };
    private static Song[] bossSongs = {
        new Song("data/music/Every Warrior has a Start.wav", "Every Warrior has a Start - Suno AI"), // Apex Slime
    };

    public static double volume = 1;
    public static Clip currentSong = null;
    public static long songTime = 1000;
    public static String lastName = "NO SONG";
    
    private static boolean boss = false;
    private static int bossMusic;

    public static void update(long elapsedTime){
        if(songTime > 0) songTime -= elapsedTime;

        if(currentSong == null || !currentSong.isActive()){
            if(boss) songTime = 0;
            if(songTime <= 0){
                // Play new song
                songTime = 180000; // 3 minutes
                if(boss){
                    currentSong = bossSongs[bossMusic].play();
                    lastName = bossSongs[bossMusic].name;
                }else{
                    if(Trident.getCurrentScene().name.equals("title")){
                        int songGroup = BTools.randInt(0, 2);
                        Song[] songArr = songs;
                        if(songGroup == 1) songArr = caveSongs;

                        int selSong = BTools.randInt(0, songArr.length);
                        currentSong = songArr[selSong].play();
                        lastName = songArr[selSong].name;
                    }else{
                        if(Background.bg == Background.SURFACE){
                            int selSong = BTools.randInt(0, songs.length);
                            currentSong = songs[selSong].play();
                            lastName = songs[selSong].name;
                        }else if(Background.bg == Background.MINES){
                            int selSong = BTools.randInt(0, caveSongs.length);
                            currentSong = caveSongs[selSong].play();
                            lastName = caveSongs[selSong].name;
                        }
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
