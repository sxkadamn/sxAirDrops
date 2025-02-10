package net.mcdrop.common.schematic.file;

import java.io.File;

public interface DirectoryManager {
    void createDirectoryIfNotExists();
    File getDirectory();
}