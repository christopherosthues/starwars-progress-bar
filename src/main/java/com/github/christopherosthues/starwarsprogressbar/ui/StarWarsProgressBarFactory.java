package com.github.christopherosthues.starwarsprogressbar.ui;

import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class StarWarsProgressBarFactory extends BasicProgressBarUI {
    @SuppressWarnings( {"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(final JComponent c) {
        c.setBorder(JBUI.Borders.empty().asUIResource());
        return new StarWarsProgressBarUI(VehiclePicker.INSTANCE.get());
    }
}
