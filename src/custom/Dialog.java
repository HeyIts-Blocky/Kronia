package custom;

import javax.sound.sampled.Clip;

import blib.util.Audio;

public class Dialog {
    
    private static Clip currentDialog = null;
    public static int dialog = -1;

    private static String[] dialogPaths = {
        "data/vo/tutorial/tutvo1.wav", // 0
        "data/vo/tutorial/tutvo2.wav", // 1
        "data/vo/tutorial/tutvo3.wav", // 2
        "data/vo/tutorial/tutvo4.wav", // 3
        "data/vo/tutorial/tutvo5.wav", // 4
        "data/vo/tutorial/tutvo6.wav", // 5
        "data/vo/tutorial/tutvo7.wav", // 6
        "data/vo/tutorial/tutvo8.wav", // 7
        "data/vo/tutorial/tutvo9.wav", // 8
        "data/vo/tutorial/tutvo10.wav", // 9
        "data/vo/tutorial/tutvo11.wav", // 10
        "data/vo/tutorial/tutvo12.wav", // 11
    };
    public static String[] dialogSubs = {
        "Alright Brandon, strapped in, ready to go? Let's load that training course, designed by yours truly, since they've got a lot of the other AnomLoc boys looking into that thing back in EB... [TYPING]", // 0
        "Let's see, first on the list is... [PAGES BEING FLIPPED] material gathering. Xenith says you gotta know this since we can't bring much more than the clothes on our backs into that place. The AnomLoc science boys say it's got something to do with the matter in the place being... fake? I don't know, Anoms are always weird like this.", // 1
        "Sweet. Now then, you should have a pickaxe in that bag of yours. Go ahead and get that out and break the rock in your way.", // 2
        "Up next is crafting. Don't worry about figuring out how to make stuff by the way. The island's got a Phen that lets you instinctively know what you can craft with any material you find. Don't ask me how, but the tech boys managed to replicate that in the sim. Anyways, go ahead and make a workbench and set it down.", // 3
        "Now then, there's also this weird Phen where you can kinda just... stand near workstations and it lets you make more things. So while you stand near the workbench, you should be able to make a furnace. Make that and put it down somewhere.", // 4
        "Alright, let's move on to getting yourself fed. We can't exactly bring rations in because of the whole... only clothes on your back thing, so you gotta get your own food. Don't worry about the cows, this one is pure programming. Even the ones on the island were studied and they aren't like any cows you might know back home. Oh, killing them with that axe would be really slow, I suggest you make yourself a wood sword. Now, let me get that door open for you... [TYPING] There you go!", // 5
        "Ah shoot, I just remembered, you need something to cook the meat! There wasn't any coal ore there, was there? Gee, what moron would forget to put coal in the room you have to cook food!? I would hate to be that guy... [SIGH] Anyways, I'll just give you some coal. [TYPING]", // 6
        "Alright now that you're fed, let's get you into some basic combat training. A wood sword's kinda weak, so there's a REAL weapon in the crate there. Go ahead and check it out.", // 7
        "Yeah sorry if you were expecting your usual firearm. I tried to get you one, but boss said I have to make it melee, since we're already so trained on ranged stuff. But it's alright, we could all use a little more practice with these swords. I'm still used to having a nice light knife, but pretty much all I've been able to make is swords in there. Anyways grab that sword and head into the next room.", // 8
        "Alright, let's put you up against your first slime, just a regular one from the surface. [TYPING]", // 9
        "Well done. You might have noticed that you can't actually get hurt. That's just because the tech boys didn't know what to do if someone died in the simulation, so they just disabled all damage. Anyways, let's get you up against a slightly stronger enemy. This one is a cave slime, it's the exact same as a regular slime, just stronger. [TYPING]", // 10
        "Alright, that about wraps it up. Let's get you out of that SimRig and prep you for infil.", // 11
    };

    public static void playDialog(int dia){
        boolean vol = false;
        if(Audio.volume == 0){ // plays the sound, just so quiet you won't be able to hear it, so that way the subtitles still work
            Audio.volume = 1;
            vol = true;
        }
        if(currentDialog != null) currentDialog.stop();

        dialog = dia;
        currentDialog = Audio.play(dialogPaths[dia]);

        if(vol) Audio.volume = 0;
    }

    public static void update(){
        if(currentDialog != null && !currentDialog.isActive()){
            dialog = -1;
            currentDialog = null;
        }
    }
}
