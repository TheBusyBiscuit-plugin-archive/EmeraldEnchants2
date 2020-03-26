package me.mrCookieSlime.EmeraldEnchants;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class IgnoredMiningEvent extends BlockBreakEvent {

    public IgnoredMiningEvent(Block theBlock, Player player) {
        super(theBlock, player);
    }

}
