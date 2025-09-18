package Data;

import Entity.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    int level;
    int exp;
    int nextLevelExp;
    int health;
    int maxHealth;
    int coin;
    int drunk;
    int maxDrunk;
    int drinkPercent;
    int speed;
    int strength;
    int dexterity;

    ArrayList<String> itemNames=new ArrayList<String>();
    ArrayList<Integer> itemAmounts=new  ArrayList<>();
    ArrayList<String> chestItemNames=new ArrayList<String>();
    ArrayList<Integer> chestItemAmounts=new  ArrayList<>();
    int currentWeaponSlot;
    int currentShieldSlot;
    int currentHelmetSlot;
    int currentChestSlot;
    int currentBootsSlot;

    String[][] mapObjectNames;
    int[][] mapObjectX;
    int[][] mapObjectY;
}
