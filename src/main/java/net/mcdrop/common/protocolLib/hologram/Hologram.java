package net.mcdrop.common.protocolLib.hologram;

import com.comphenix.protocol.events.PacketEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Hologram {

    private final List<HologramLine> hologramLines = new ArrayList<>();
    private final Location baseLocation;

    public Hologram(List<String> lines, Location location) {
        this.baseLocation = location;
        setLines(lines);
    }

    public Hologram(String line, Location location) {
        this.baseLocation = location;
        setLine(line);
    }

    public void show(Player player) {
        hologramLines.forEach(line -> line.show(player));
    }

    public void showAll() {
        hologramLines.forEach(HologramLine::showAll);
    }

    public void unShow(Player player) {
        hologramLines.forEach(line -> line.unShow(player));
    }

    public void unShowAll() {
        hologramLines.forEach(HologramLine::unShowAll);
    }

    public void setClickAction(BiConsumer<Player, PacketEvent> onClick) {
        hologramLines.forEach(line -> line.setClickAction(onClick));
    }

    public void teleport(Location newLocation) {
        for (int i = 0; i < hologramLines.size(); i++) {
            HologramLine line = hologramLines.get(i);
            line.teleport(newLocation.clone().add(0, i * 0.3D, 0));
        }
    }

    public void remove() {
        hologramLines.forEach(HologramLine::removeLine);
        hologramLines.clear();
    }

    public void setLines(List<String> lines) {
        remove();
        double offset = 0;
        for (String text : lines) {
            if (!text.trim().isEmpty()) {
                Location lineLocation = baseLocation.clone().add(0, offset, 0);
                hologramLines.add(new HologramLine(text, lineLocation));
                offset += 0.3D;
            }
        }
        showAll();
    }

    public void setLine(String line) {
        remove();
        hologramLines.add(new HologramLine(line, baseLocation));
        showAll();
    }

    public String getLine() {
        return hologramLines.isEmpty() ? null : hologramLines.get(0).getLineName();
    }

    public void addLine(String text) {
        double offset = hologramLines.size() / 0.3D;
        Location lineLocation = baseLocation.clone().add(0, offset, 0);
        hologramLines.add(new HologramLine(text, lineLocation));
        hologramLines.get(hologramLines.size() - 1).showAll();
    }

    public List<HologramLine> getLines() {
        return hologramLines;
    }

    public Location getBaseLocation() {
        return baseLocation;
    }
}
