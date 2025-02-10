package net.mcdrop.common.schematic.dimension.utility;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import net.mcdrop.sxAirDrops;
import net.mcdrop.common.schematic.dimension.Dimensions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DimensionUtility {
    public Dimensions getSchematicDimensions(File schematicFile) {
        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if (format == null) {
            throw new IllegalArgumentException(sxAirDrops.getInstance().getConfig()
                    .getString("messages.unsupported_schematic_format")
                    .replace("{filename}", schematicFile.getName()));
        }

        try (ClipboardReader reader = format.getReader(Files.newInputStream(schematicFile.toPath()))) {
            Clipboard clipboard = reader.read();
            BlockVector3 dimensions = clipboard.getDimensions();
            return new Dimensions(dimensions.getBlockX(), dimensions.getBlockY(), dimensions.getBlockZ());
        } catch (IOException e) {
            throw new RuntimeException(sxAirDrops.getInstance().getConfig()
                    .getString("messages.error_reading_schematic")
                    .replace("{filename}", schematicFile.getName()), e);
        }
    }

}
