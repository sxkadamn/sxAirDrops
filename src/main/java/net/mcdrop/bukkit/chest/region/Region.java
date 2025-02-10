package net.mcdrop.bukkit.chest.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import net.mcdrop.sxAirDrops;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Region {

    private static final Map<String, String> regions = new HashMap<>();
    private static final String CHARACTERS = "ABCVDASPEDASKELOQIKSAlowdkqmcabcdaewqdsa";
    private static final Random RANDOM = new Random();

    /**
     * Создаёт регион с указанными начальной и конечной точками, присваивает имя и применяет флаги.
     *
     * @param start Начальная точка региона.
     * @param end Конечная точка региона.
     * @param name Имя региона.
     */
    public void createRegion(Location start, Location end, String name) {
        World world = end.getWorld();
        if (world == null) return;

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager == null) return;

        ProtectedCuboidRegion protectedRegion = new ProtectedCuboidRegion(name, convertToBlockVector(start), convertToBlockVector(end));
        regionManager.addRegion(protectedRegion);

        String worldName = world.getName();

        // Чтение флагов из конфигурации
        List<Map<?, ?>> flags = sxAirDrops.getInstance().getConfig().getMapList("event.tools.flags");
        for (Map<?, ?> flagEntry : flags) {
            for (Map.Entry<?, ?> entry : flagEntry.entrySet()) {
                String flag = (String) entry.getKey();
                String value = (String) entry.getValue();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "region flag -w " + worldName + " " + name + " " + flag + " " + value);
            }
        }

        // Отправка сообщения о создании региона
        Bukkit.getLogger().info(sxAirDrops.getInstance().getConfig().getString("messages.region_created")
                .replace("{name}", name));
    }

    /**
     * Создаёт регион вокруг блока с учётом настроек из конфигурации.
     *
     * @param block Блок, вокруг которого будет создан регион.
     */
    public void regionCreate(Block block) {
        if (!sxAirDrops.getInstance().getConfig().getBoolean("event.tools.region.enable")) {
            return;
        }

        int regionSize = sxAirDrops.getInstance().getConfig().getInt("event.tools.region.size");
        Location min = block.getLocation().clone().add(regionSize, 255, regionSize);
        Location max = block.getLocation().clone().subtract(regionSize, 100, regionSize);
        String name = generateRandomString(20);

        regions.put("lieRegion", name);
        createRegion(min, max, name);
    }

    /**
     * Генерирует случайную строку заданной длины.
     *
     * @param length Длина строки.
     * @return Случайная строка.
     */
    public String generateRandomString(int length) {
        StringBuilder text = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            text.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return text.toString();
    }

    /**
     * Преобразует объект Location в BlockVector3 для WorldEdit.
     *
     * @param location Локация, которую нужно преобразовать.
     * @return Соответствующий BlockVector3.
     */
    public BlockVector3 convertToBlockVector(Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Удаляет регион по имени в указанном мире.
     *
     * @param name Имя региона.
     * @param world Мир, где существует регион.
     */
    public void removeRegion(String name, World world) {
        if (world == null) return;

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager != null) {
            regionManager.removeRegion(name);
            // Сообщение об удалении региона
            Bukkit.getLogger().info(sxAirDrops.getInstance().getConfig().getString("messages.region_removed")
                    .replace("{name}", name));
        } else {
            // Логирование ошибки, если не найден менеджер регионов
            Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig().getString("messages.region_error"));
        }
    }

    public String getRegion() {
        return regions.values().stream().findFirst().orElse(null);
    }
}
