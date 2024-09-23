package custom;

import java.util.ArrayList;

import blib.util.BTools;
import ent.GameObject;
import trident.TridEntity;
import trident.Trident;
public class Recipe {

    public Item[] ingredients;
    public Item output;
    int workstation = -1;
    
    public Recipe(Item[] ing, Item out){
        ingredients = ing;
        output = out;
    }
    public Recipe(Item[] ing, Item out, int station){
        ingredients = ing;
        output = out;
        workstation = station;
    }

    public boolean canCraft(){
        try {
            boolean good = true;
            if(workstation != -1){
                good = false;
                for(int i = 0; i < Trident.getEntities().size(); i++){
                    TridEntity e = Trident.getEntities().get(i);
                    if(e instanceof GameObject){
                        GameObject go = (GameObject)e;
                        if(go.id == workstation && BTools.getDistance(e.position, Trident.getPlrPos()) < 128){
                            good = true;
                            break;
                        }
                    }
                }
            }
            if(!good) return false;

            boolean[] hasIngredient = new boolean[ingredients.length];
            for(int i = 0; i < hasIngredient.length; i++) hasIngredient[i] = false;
            for(int i2 = 0; i2 < GameData.inventory.length; i2++){
                Item i = GameData.inventory[i2];
                for(int j = 0; j < ingredients.length; j++){
                    Item ing = ingredients[j];
                    if(ing.id == i.id && i.amount >= ing.amount){
                        hasIngredient[j] = true;
                    }
                }
            }

            for(boolean b: hasIngredient) if(!b) return false;

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canSeeRecipe(){
        if(output.id == Item.SCP999){
            return canCraft();
        }

        boolean good = true;
        if(workstation != -1){
            good = false;
            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                if(e instanceof GameObject){
                    GameObject go = (GameObject)e;
                    if(go.id == workstation && BTools.getDistance(e.position, Trident.getPlrPos()) < 128){
                        good = true;
                        break;
                    }
                }
            }
        }
        if(!good) return false;

        for(int i2 = 0; i2 < GameData.inventory.length; i2++){
            Item i = GameData.inventory[i2];
            for(int j = 0; j < ingredients.length; j++){
                Item ing = ingredients[j];
                if(ing.id == i.id && i.amount >= 1){
                    return true;
                }
            }
        }

        return false;
    }

    public Item craft(){
        if(canCraft()){
            boolean[] hasIngredient = new boolean[ingredients.length];
            for(int i = 0; i < hasIngredient.length; i++) hasIngredient[i] = false;
            for(Item i: GameData.inventory){
                for(int j = 0; j < ingredients.length; j++){
                    Item ing = ingredients[j];
                    if(ing.id == i.id && i.amount >= ing.amount && !hasIngredient[j]){
                        hasIngredient[j] = true;
                        i.amount -= ing.amount;
                    }
                }
            }
            return output.copy();
        }else{
            return null;
        }
    }



    public static ArrayList<Recipe> getRecipes(){
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        for(int i = 0; i < recipes.length; i++){
            if(recipes[i].canSeeRecipe()){
                list.add(recipes[i]);
            }
        }
        return list;
    }

    private static Recipe[] recipes = {
        new Recipe(new Item[]{new Item(Item.WOOD, 10)}, new Item(Item.WORKBENCH)),
        new Recipe(new Item[]{new Item(Item.WOOD, 15)}, new Item(Item.W_SWORD), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 10)}, new Item(Item.W_PICK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 10)}, new Item(Item.W_AXE), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.STONE, 20)}, new Item(Item.FURNACE), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 10)}, new Item(Item.COAL), GameObject.FURNACE),
        new Recipe(new Item[]{new Item(Item.RAWMEAT), new Item(Item.COAL)}, new Item(Item.COOKEDMEAT), GameObject.FURNACE),
        new Recipe(new Item[]{new Item(Item.WOOD, 10)}, new Item(Item.LOGWALL), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD), new Item(Item.JELLY)}, new Item(Item.TORCH)),
        new Recipe(new Item[]{new Item(Item.JELLY, 999)}, new Item(Item.SCP999)),
        new Recipe(new Item[]{new Item(Item.W_SWORD), new Item(Item.STONE, 5)}, new Item(Item.S_SWORD), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 7), new Item(Item.STONE, 5)}, new Item(Item.S_PICK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 7), new Item(Item.STONE, 5)}, new Item(Item.S_AXE), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.JELLY, 30)}, new Item(Item.APEXSPAWN), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 10)}, new Item(Item.BOW), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD), new Item(Item.STONE)}, new Item(Item.ARROW, 10), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 5), new Item(Item.HARDJELLY, 5)}, new Item(Item.J_SWORD), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 5), new Item(Item.HARDJELLY, 5)}, new Item(Item.J_PICK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 5), new Item(Item.HARDJELLY, 5)}, new Item(Item.J_AXE), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 5), new Item(Item.HARDJELLY, 5)}, new Item(Item.J_SHOVEL), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.J_SHOVEL)}, new Item(Item.EMERGENCYPOGO)),
        new Recipe(new Item[]{new Item(Item.EMERGENCYPOGO)}, new Item(Item.J_SHOVEL)),
        new Recipe(new Item[]{new Item(Item.STONE, 10)}, new Item(Item.STONELADDER)),
        new Recipe(new Item[]{new Item(Item.IRONORE, 3), new Item(Item.COAL)}, new Item(Item.IRONINGOT), GameObject.FURNACE),
        new Recipe(new Item[]{new Item(Item.COPPERORE, 3), new Item(Item.COAL)}, new Item(Item.COPPERINGOT), GameObject.FURNACE),
        new Recipe(new Item[]{new Item(Item.IRONINGOT, 10), new Item(Item.COAL, 3)}, new Item(Item.ANVIL), GameObject.FURNACE),
        new Recipe(new Item[]{new Item(Item.COPPERINGOT, 10), new Item(Item.COAL, 3)}, new Item(Item.ANVIL), GameObject.FURNACE),
        new Recipe(new Item[]{new Item(Item.COPPERINGOT, 5), new Item(Item.WOOD, 3)}, new Item(Item.C_SWORD), GameObject.ANVIL),
        new Recipe(new Item[]{new Item(Item.COPPERINGOT, 5), new Item(Item.WOOD, 3)}, new Item(Item.C_PICK), GameObject.ANVIL),
        new Recipe(new Item[]{new Item(Item.COPPERINGOT, 5), new Item(Item.WOOD, 3)}, new Item(Item.C_AXE), GameObject.ANVIL),
        new Recipe(new Item[]{new Item(Item.IRONINGOT, 5), new Item(Item.WOOD, 3)}, new Item(Item.I_SWORD), GameObject.ANVIL),
        new Recipe(new Item[]{new Item(Item.IRONINGOT, 5), new Item(Item.WOOD, 3)}, new Item(Item.I_PICK), GameObject.ANVIL),
        new Recipe(new Item[]{new Item(Item.IRONINGOT, 5), new Item(Item.WOOD, 3)}, new Item(Item.I_AXE), GameObject.ANVIL),
        new Recipe(new Item[]{new Item(Item.IRONINGOT), new Item(Item.WOOD, 10)}, new Item(Item.I_ARROW, 10), GameObject.ANVIL),
        new Recipe(new Item[]{new Item(Item.ARROW), new Item(Item.JELLY)}, new Item(Item.J_ARROW), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.I_ARROW, 20), new Item(Item.HARDJELLY)}, new Item(Item.HJ_ARROW, 20), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.TUTWOOD, 30)}, new Item(Item.I_PICK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 10), new Item(Item.TORCH)}, new Item(Item.CAMPFIRE)),
        new Recipe(new Item[]{new Item(Item.RAWMEAT), new Item(Item.WOOD)}, new Item(Item.HOTDOG), GameObject.CAMPFIRE),
        new Recipe(new Item[]{new Item(Item.WOOD, 20)}, new Item(Item.CRATE), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.JELLY, 5)}, new Item(Item.FLASK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.FLASK), new Item(Item.COOKEDMEAT, 5), new Item(Item.JELLY, 10)}, new Item(Item.HP_FLASK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.FLASK), new Item(Item.HP_FLASK, 3), new Item(Item.POISONFLASK, 5)}, new Item(Item.REGEN_FLASK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.FLASK), new Item(Item.S_SWORD), new Item(Item.JELLY, 15)}, new Item(Item.STR_FLASK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.FLASK), new Item(Item.TORCH, 5), new Item(Item.JELLY, 5)}, new Item(Item.GLOW_FLASK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.FLASK), new Item(Item.MUSHROOMCHUNK, 5)}, new Item(Item.POISONFLASK), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.J_ARROW, 10), new Item(Item.TORCH)}, new Item(Item.FIRE_ARROW, 10)),
        new Recipe(new Item[]{new Item(Item.J_ARROW, 10)}, new Item(Item.FIRE_ARROW, 10), GameObject.CAMPFIRE),
        new Recipe(new Item[]{new Item(Item.WOOD, 2), new Item(Item.COPPERINGOT, 10)}, new Item(Item.WIRE), GameObject.FURNACE),
        new Recipe(new Item[]{new Item(Item.WIRE, 3), new Item(Item.ANTENNA, 2), new Item(Item.IRONINGOT, 5)}, new Item(Item.WIRE), GameObject.WORKBENCH),
        new Recipe(new Item[]{new Item(Item.WOOD, 5), new Item(Item.MUSHROOMCHUNK, 4), new Item(Item.RAWMEAT, 2)}, new Item(Item.MUSHROOMSKEWER), GameObject.CAMPFIRE),
    };
}
