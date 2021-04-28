/* Original code allows signs to push commands - As OP you can create them by adding [CMD] at the first line.
Type the command in the second line. After that TAP the sign and DONE. The sign listens for hits as Player.
Edited to change the contents of a sign based on variables assigned in the Answers class. */

package Events;

import Reference.Answers;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class CommandSigns extends PluginBase implements Listener {
    public CommandSigns(Config config) {
    }

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("Command Signs enabled...");
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void execute(PlayerInteractEvent e) {
        if (e != null) {
            Player p = e.getPlayer();
            Block b = e.getBlock();
            Action action = e.getAction();
            if (b.getId() == 68 || b.getId() == 63) {
                if (!(p.getLevel().getBlockEntity(b.getLocation()) instanceof BlockEntitySign)) {
                    return;
                }

                BlockEntitySign bes = (BlockEntitySign)p.getLevel().getBlockEntity(b.getLocation());
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    String[] signtext = bes.getText();
                    String line1 = "";
                    String line2 = "";
                    if (signtext.length > 1) {
                        line1 = signtext[0];
                        line2 = signtext[1];
                        // Can the below be done using switch instead? Feel like it would be neater.
                        if (line1.equals("[CMD]") && p.isOp()) {
                            bes.setText(new String[]{"§a§c§e§c§b§r[§a§c§e§c§bCommand§r]", "§a" + line2});
                        }
                        else if (line1.equals("§a§c§e§c§b§r[§a§c§e§c§bCommand§r]")) {
                            p.getServer().dispatchCommand(p, line2.replaceAll("§a", ""));
                        }
                        else if (line2.equals("§aCORRECT!")) {
                            bes.setText(new String[]{"", "§aCORRECT!"});
                        }
                        else if (line1.equals(Answers.answer1)) {
                            bes.setText(new String[]{"", "§aCORRECT!"});
                        }

                        else {
                            bes.setText(new String[]{"", "§cBad luck!"});
                        }
                        e.setCancelled(true);
                    }
                }
            }

        }
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onEvent(BlockBreakEvent e) {
        if (e != null) {
            Player p = e.getPlayer();
            Block b = e.getBlock();
            if (b.getId() == 68 || b.getId() == 63) {
                if (!(p.getLevel().getBlockEntity(b.getLocation()) instanceof BlockEntitySign)) {
                    return;
                }

                BlockEntitySign bes = (BlockEntitySign)p.getLevel().getBlockEntity(b.getLocation());
                String[] signtext = bes.getText();
                String line1 = "";
                if (signtext.length > 0) {
                    line1 = signtext[0];
                    if (line1.equals("§a§c§e§c§b§r[§a§c§e§c§bCommand§r]") && !p.isOp() || line1.equals("§a§c§e§c§b§r[§a§c§e§c§bCommand§r]") && p.isOp() && !p.isSneaking()) {
                        e.setCancelled(true);
                    }
                }
            }

        }
    }
}
