package com.maximde.damageindicator;

import com.maximde.betterchatbubbles.api.BubbleAPI;
import com.maximde.betterchatbubbles.api.ChatBubble;
import com.maximde.betterchatbubbles.api.RenderMode;
import com.maximde.betterchatbubbles.api.utils.Vector3D;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


public final class DamageIndicator extends JavaPlugin implements Listener {

    private BubbleAPI bubbleAPI;
    private Config config;

    @Override
    public void onEnable() {
        if(BubbleAPI.getBubbleAPI().isEmpty()) throw new RuntimeException("Failed to load Fancy Damage Indicator! Reason: API instance is empty!");
        bubbleAPI = BubbleAPI.getBubbleAPI().get();
        this.config = new Config();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("fancydamage").setExecutor(new FancyDamageCommand(config));
        getCommand("fancydamage").setTabCompleter((sender, cmd, alias, args) -> {
            if (args.length == 1) {
                return List.of("reload");
            }
            return null;
        });
    }

    @EventHandler
    public void onEntityRegem(EntityRegainHealthEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;

        if(config.getType() == Config.Types.ONLY_DAMAGE) return;

        if(config.getAffectedEntities() == Config.AffectedEntities.ONLY_PLAYERS) {
            if(!(event.getEntity() instanceof Player)) return;
        }

        if(config.getAffectedEntities() == Config.AffectedEntities.ONLY_MOBS) {
            if((event.getEntity() instanceof Player)) return;
        }

        ChatBubble chatBubble = new ChatBubble((LivingEntity) event.getEntity(), RenderMode.NEARBY);
        chatBubble.setDuration(config.getDurationSeconds());
        chatBubble.setShadow(config.isTextShadow());


        String damage;
        if (config.getHealthMode() == Config.HealthMode.NUMBER) {
            damage = String.valueOf((int) event.getAmount());
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (int) event.getAmount(); i++) {
                sb.append(config.getHeartCharacter());
            }
            damage = sb.toString();
        }

        chatBubble.setScale(new Vector3D(config.getSize(), config.getSize(), config.getSize()));
        chatBubble.setMiniMessageText(config.getRegenString().replace("%regen%", damage));


        if(config.isDamageSizeModifier()) {
            chatBubble.setScale(chatBubble.getScale().multiply((float) (Math.max(event.getAmount() / 10, 0.5F) * config.getDamageSizeModifier())));
        }

        bubbleAPI.bubbleGenerator().spawnBubble(chatBubble);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;

        if(config.getType() == Config.Types.ONLY_REGEN) return;

        if(config.getAffectedEntities() == Config.AffectedEntities.ONLY_PLAYERS) {
            if(!(event.getEntity() instanceof Player)) return;
        }

        if(config.getAffectedEntities() == Config.AffectedEntities.ONLY_MOBS) {
            if((event.getEntity() instanceof Player)) return;
        }

        ChatBubble chatBubble = new ChatBubble((LivingEntity) event.getEntity(), RenderMode.NEARBY);
        chatBubble.setDuration(config.getDurationSeconds());
        chatBubble.setShadow(config.isTextShadow());


        String damage;
        if (config.getHealthMode() == Config.HealthMode.NUMBER) {
            damage = String.valueOf((int) event.getDamage());
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (int) event.getDamage(); i++) {
                sb.append(config.getHeartCharacter());
            }
            damage = sb.toString();
        }

        chatBubble.setScale(new Vector3D(config.getSize(), config.getSize(), config.getSize()));
                chatBubble.setMiniMessageText(config.getDamageString().replace("%damage%", damage));


        if(config.isDamageSizeModifier()) {
            chatBubble.setScale(chatBubble.getScale().multiply((float) (Math.max(event.getDamage() / 10, 0.5F) * config.getDamageSizeModifier())));
        }

        bubbleAPI.bubbleGenerator().spawnBubble(chatBubble);
    }
}
