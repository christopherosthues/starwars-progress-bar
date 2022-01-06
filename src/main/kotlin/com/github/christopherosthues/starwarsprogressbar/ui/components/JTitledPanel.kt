package com.github.christopherosthues.starwarsprogressbar.ui.components

import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

internal class JTitledPanel(title: String) : JPanel() {
    private val titleLabel: JLabel = JLabel(title)

    val contentPanel: JPanel = JPanel()

    var title: String
        get() = titleLabel.text
        set(value) {
            titleLabel.text = value
        }

    init {
        super.setLayout(BorderLayout(0, 10))

        val titlePanel = JPanel()
        titlePanel.layout = GridBagLayout()

        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = 0
        gridBagConstraints.weighty = 0.5
        gridBagConstraints.anchor = GridBagConstraints.WEST

        titlePanel.add(titleLabel, gridBagConstraints)

        gridBagConstraints.gridx = 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 0.5
        gridBagConstraints.insets = Insets(0, 10, 0, 0)

        titlePanel.add(JSeparator(SwingConstants.HORIZONTAL), gridBagConstraints)

        contentPanel.border = EmptyBorder(0, 15, 25, 5)

        super.add(titlePanel, BorderLayout.NORTH)
        super.add(contentPanel, BorderLayout.CENTER)
    }

    override fun getLayout(): LayoutManager {
        return contentPanel?.layout
    }

    override fun setLayout(mgr: LayoutManager?) {
        contentPanel?.layout = mgr
    }

    override fun add(comp: Component?): Component {
        return contentPanel.add(comp)
    }

    override fun add(comp: Component, constraints: Any?) {
        contentPanel.add(comp, constraints)
    }

    override fun add(comp: Component?, index: Int): Component {
        return contentPanel.add(comp, index)
    }

    override fun add(comp: Component?, constraints: Any?, index: Int) {
        contentPanel.add(comp, constraints, index)
    }

    override fun add(name: String?, comp: Component?): Component {
        return contentPanel.add(name, comp)
    }
}