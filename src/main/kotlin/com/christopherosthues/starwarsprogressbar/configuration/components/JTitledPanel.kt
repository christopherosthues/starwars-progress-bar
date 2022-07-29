package com.christopherosthues.starwarsprogressbar.configuration.components

import java.awt.BorderLayout
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.LayoutManager
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.SwingConstants
import javax.swing.border.EmptyBorder

private const val VERTICAL_GAP = 10
private const val SEPARATOR_LEFT_PADDING = 10
private const val TITLE_Y_WEIGHT = 0.5
private const val CONTENT_LEFT_PADDING = 15
private const val CONTENT_RIGHT_PADDING = 5
private const val CONTENT_BOTTOM_PADDING = 25

internal open class JTitledPanel(title: String) : JPanel() {
    private val titleLabel: JLabel = JLabel(title)

    val contentPanel: JPanel = JPanel()

    var title: String
        get() = titleLabel.text
        set(value) {
            titleLabel.text = value
        }

    init {
        super.setLayout(BorderLayout(0, VERTICAL_GAP))

        val titlePanel = JPanel()
        titlePanel.layout = GridBagLayout()

        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = 0
        gridBagConstraints.weighty = TITLE_Y_WEIGHT
        gridBagConstraints.anchor = GridBagConstraints.WEST

        titlePanel.add(titleLabel, gridBagConstraints)

        gridBagConstraints.gridx = 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = TITLE_Y_WEIGHT
        gridBagConstraints.insets = Insets(0, SEPARATOR_LEFT_PADDING, 0, 0)

        titlePanel.add(JSeparator(SwingConstants.HORIZONTAL), gridBagConstraints)

        contentPanel.border = EmptyBorder(0, CONTENT_LEFT_PADDING, CONTENT_BOTTOM_PADDING, CONTENT_RIGHT_PADDING)

        super.add(titlePanel, BorderLayout.NORTH)
        super.add(contentPanel, BorderLayout.CENTER)
    }

    override fun getLayout(): LayoutManager? {
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
