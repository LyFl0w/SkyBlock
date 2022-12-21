package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.SkyBlock;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class Challenge<T extends Event> implements Listener {

    protected final SkyBlock skyBlock;

    protected final int id;
    protected final Difficulty difficulty;
    protected final Type type;
    protected final Status status;

    protected Challenge(SkyBlock skyBlock, int id, Difficulty difficulty, Type type, Status status) {
        this.skyBlock = skyBlock;
        this.id = id;
        this.difficulty = difficulty;
        this.type = type;
        this.status = status;
    }

    @EventHandler
    public abstract void onEvent(T event);

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public enum Difficulty {
        EASY(2, null), MEDIUM(3, null), NORMAL(4, null), HARD(5, null), EXTREME(6, null);

        private final ItemStack itemStack;
        private final int slot;

        Difficulty(int slot,ItemStack itemStack) {
            this.itemStack = itemStack;
            this.slot = slot;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getSlot() {
            return slot;
        }
    }

    public enum Type {
        KILL_ENTITY,
        REPRODUCE_ANIMAL,

        PLACE_BLOCK,
        REMOVE_BLOCK,

        CRAFT_ITEM,

        BUY_ITEM,
        SELL_ITEM,
    }

    public enum Status {
        LOCKED, CURRENT, FINISH
    }

}
