package com.github.christopherosthues.starwarsprogressbar.listeners;

import com.github.christopherosthues.starwarsprogressbar.ui.StarWarsProgressBarFactory;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public class StarWarsProgressListener implements LafManagerListener, DynamicPluginListener {
    private static final String PROGRESS_BAR_UI_KEY = "ProgressBarUI";
    private static final String STAR_WARS_PROGRESS_BAR_UI_IMPLEMENTATION_NAME = StarWarsProgressBarFactory.class.getName();
    private volatile static Object previousProgressBar = null;

    public StarWarsProgressListener() {
        updateProgressBarUi();
    }

    @Override
    public void lookAndFeelChanged(@NotNull LafManager source) {
        updateProgressBarUi();
    }

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        updateProgressBarUi();
    }

    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        resetProgressBarUi();
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
