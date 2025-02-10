package net.mcdrop.common.schematic.manage;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.mcdrop.sxAirDrops;
import net.mcdrop.common.schematic.IEditSessionManager;
import net.mcdrop.common.schematic.ISchematicManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SchematicManager implements ISchematicManager {
    private final IEditSessionManager editSessionManager;

    public SchematicManager(IEditSessionManager editSessionManager) {
        this.editSessionManager = editSessionManager;
    }

    @Override
    public void pasteSchematic(Location location, File schematicFile) {
        if (!sxAirDrops.getInstance().getConfig().getBoolean("event.tools.schematic.enable")) {
            return;
        }

        if (location == null || schematicFile == null || !schematicFile.exists()) {
            Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig()
                    .getString("messages.invalid_location_or_file"));
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if (format == null) {
            Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig()
                    .getString("messages.unsupported_schematic_format")
                    .replace("{file}", schematicFile.getName()));
            return;
        }

        try (ClipboardReader reader = format.getReader(Files.newInputStream(schematicFile.toPath()))) {
            Clipboard clipboard = reader.read();
            World adaptedWorld = BukkitAdapter.adapt(location.getWorld());

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(adaptedWorld)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
                editSessionManager.addSession(editSession);
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe(sxAirDrops.getInstance().getConfig()
                    .getString("messages.error_reading_schematic")
                    .replace("{error}", e.getMessage()));
            e.printStackTrace();
        } catch (com.sk89q.worldedit.WorldEditException e) {
            Bukkit.getLogger().severe(sxAirDrops.getInstance().getConfig()
                    .getString("messages.error_pasting_schematic")
                    .replace("{error}", e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public void undoLastPaste() {
        editSessionManager.undoLastSession();
    }
}