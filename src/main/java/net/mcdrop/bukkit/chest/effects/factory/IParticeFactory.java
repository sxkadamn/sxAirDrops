package net.mcdrop.bukkit.chest.effects.factory;

import net.mcdrop.bukkit.chest.effects.IParticalSpiral;
import net.mcdrop.bukkit.chest.effects.IParticleCreative;
import net.mcdrop.bukkit.chest.effects.IParticleRod;
import net.mcdrop.bukkit.chest.effects.ITypeParticle;

import java.util.HashMap;
import java.util.Map;

public class IParticeFactory {
    private static final Map<String, ITypeParticle> effectMap = new HashMap<>();

    static {
        effectMap.put("ROD", new IParticleRod());
        effectMap.put("SPIRAL", new IParticalSpiral());
        effectMap.put("CREATIVE", new IParticleCreative());
    }

    public static ITypeParticle getParticleEffect(String effectName) {
        return effectMap.getOrDefault(effectName.toUpperCase(), null);
    }
}
