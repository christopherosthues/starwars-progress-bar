/**
 * MIT License
 *
 * Copyright 2020-2021 Karl Goffin
 * Copyright 2022 Christopher Osthues
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.christopherosthues.starwarsprogressbar.listeners;

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants;
import com.christopherosthues.starwarsprogressbar.ui.StarWarsProgressBarFactory;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public class StarWarsProgressListener implements LafManagerListener, DynamicPluginListener {
    private static final String PROGRESS_BAR_UI_KEY = "ProgressBarUI";
    private static final String STAR_WARS_PROGRESS_BAR_UI_IMPLEMENTATION_NAME = StarWarsProgressBarFactory.class.getName();
    private volatile static Object previousProgressBar = null;
    private volatile static PluginId pluginId = null;

    public StarWarsProgressListener() {
        updateProgressBarUi();
        pluginId = PluginId.getId(PluginConstants.PluginId);
    }

    @Override
    public void lookAndFeelChanged(@NotNull LafManager source) {
        updateProgressBarUi();
    }

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        if (pluginId.equals(pluginDescriptor.getPluginId())) {
            updateProgressBarUi();
        }
    }

    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        if (pluginId.equals(pluginDescriptor.getPluginId())) {
            resetProgressBarUi();
        }
    }

    static void updateProgressBarUi() {
        final Object prev = UIManager.get(PROGRESS_BAR_UI_KEY);
        if (!Objects.equals(STAR_WARS_PROGRESS_BAR_UI_IMPLEMENTATION_NAME, prev)) {
            previousProgressBar = prev;
        }

        UIManager.put(PROGRESS_BAR_UI_KEY, STAR_WARS_PROGRESS_BAR_UI_IMPLEMENTATION_NAME);
        UIManager.getDefaults().put(STAR_WARS_PROGRESS_BAR_UI_IMPLEMENTATION_NAME, StarWarsProgressBarFactory.class);
    }

    static void resetProgressBarUi() {
        UIManager.put(PROGRESS_BAR_UI_KEY, previousProgressBar);
    }
}
