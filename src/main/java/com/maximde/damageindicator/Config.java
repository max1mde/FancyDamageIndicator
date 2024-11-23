package com.maximde.damageindicator;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

@Getter
public class Config {
    private final File file = new File("plugins/FancyDamageIndicator", "config.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    private HealthMode healthMode;
    private String heartCharacter;
    private String damageString;
    private String regenString;
    private Types type;

    private boolean isDamageSizeModifier;
    private float damageSizeModifier;

    private float size;
    private int durationSeconds;

    private boolean textShadow;
    private boolean sound;
    private AffectedEntities affectedEntities;

    private List<String> mobBlacklist;

    public Config() {
        setIfNot("MobBlacklist", List.of("ARMOR_STAND"));
        setIfNot("HealthMode", HealthMode.NUMBER.name());
        setIfNot("HeartCharacter", "❤");
        setIfNot("DamageString", "<gradient:#FF4700:#FF0070>%damage% ❤</gradient>");
        setIfNot("RegenString", "<gradient:#7DFF00:#00D45C>%regen% ❤</gradient>");
        setIfNot("Type", Types.ALL.name());
        setIfNot("IsDamageSizeModifier", true);
        setIfNot("DamageSizeModifier", 1.0f);
        setIfNot("Size", 1.0f);
        setIfNot("Sound", false);
        setIfNot("DurationSeconds", 1);
        setIfNot("TextShadow", true);
        setIfNot("AffectedEntities", AffectedEntities.ALL.name());
        saveConfig();
        initValues();
    }

    private void initValues() {
        this.mobBlacklist = cfg.getStringList("MobBlacklist");
        this.healthMode = HealthMode.valueOf(cfg.getString("HealthMode"));
        this.heartCharacter = cfg.getString("HeartCharacter");
        this.damageString = cfg.getString("DamageString");
        this.regenString = cfg.getString("RegenString");
        this.type = Types.valueOf(cfg.getString("Type"));
        this.isDamageSizeModifier = cfg.getBoolean("IsDamageSizeModifier");
        this.damageSizeModifier = (float) cfg.getDouble("DamageSizeModifier");
        this.size = (float) cfg.getDouble("Size");
        this.durationSeconds = cfg.getInt("DurationSeconds");
        this.textShadow = cfg.getBoolean("TextShadow");
        this.sound = cfg.getBoolean("Sound");
        this.affectedEntities = AffectedEntities.valueOf(cfg.getString("AffectedEntities"));
    }

    public enum HealthMode {
        HEARTS,
        NUMBER
    }

    public enum AffectedEntities {
        ALL,
        ONLY_PLAYERS,
        ONLY_MOBS
    }

    public void reload() {
        this.cfg = YamlConfiguration.loadConfiguration(file);
        initValues();
    }

    @SneakyThrows
    public void saveConfig() {
        cfg.save(file);
    }

    private void setIfNot(String path, Object value) {
        if(!cfg.isSet(path)) setValue(path, value);
    }

    public void setValue(String path, Object value) {
        this.cfg.set(path, value);
        saveConfig();
    }

    public Object getValue(String path) {
        return this.cfg.get(path);
    }

    public enum Types {
        ONLY_REGEN,
        ONLY_DAMAGE,
        ALL
    }

    public boolean isMobBlacklisted(String entityType) {
        return mobBlacklist.contains(entityType);
    }
}
