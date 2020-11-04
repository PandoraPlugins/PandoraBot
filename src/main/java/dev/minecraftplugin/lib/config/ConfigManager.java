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
import com.google.gson.GsonBuilder;

@SuppressWarnings("all")
public class ConfigManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public <T> Config<T> loadConfig(String name, T defaults) {
        return new Config<T>(name, gson, defaults);
    }

    public Gson getGson() {
        return gson;
    }
}


