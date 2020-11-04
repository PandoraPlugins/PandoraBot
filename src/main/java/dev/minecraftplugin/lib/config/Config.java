/*
 * An open source utilities library used for Azortis plugins.
 *     Copyright (C) 2019  Azortis
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.minecraftplugin.lib.config;

import com.google.gson.Gson;
import dev.minecraftplugin.PandoraBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("all")
public class Config<T> {
    private final Gson gson;
    private final File configurationFile;
    private T configuration;

    public Config(String name, Gson gson, T defaults) {
        this.gson = gson;
        File jarPath = new File(PandoraBot.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath());
        configurationFile = new File(jarPath.getParentFile().getAbsolutePath(), name.endsWith(".json") ? name : name + ".json");
        try {
            if (!jarPath.exists()) jarPath.mkdirs();
            if (!configurationFile.exists()) {
                configurationFile.mkdirs();
                configurationFile.createNewFile();
                configuration = defaults;
                saveConfig();
            } else
                configuration = (T) gson.fromJson(new FileReader(configurationFile), defaults.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getConfigurationFile() {
        return configurationFile;
    }

    public T getConfiguration() {
        return configuration;
    }

    public void saveConfig() {
        try {
            final String json = gson.toJson(configuration);
            configurationFile.delete();
            Files.write(configurationFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            configuration = (T) gson.fromJson(new FileReader(configurationFile), configuration.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
