package com.christopherosthues.starwarsprogressbar.configuration.components

import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.LayoutManager
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.SwingConstants

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
        gridBagConstraints.insets = JBUI.insetsLeft(SEPARATOR_LEFT_PADDING)

        titlePanel.add(JSeparator(SwingConstants.HORIZONTAL), gridBagConstraints)

        contentPanel.border = JBUI.Borders.empty(0, CONTENT_LEFT_PADDING, CONTENT_BOTTOM_PADDING, CONTENT_RIGHT_PADDING)

        super.add(titlePanel, BorderLayout.NORTH)
        super.add(contentPanel, BorderLayout.CENTER)
    }

    override fun getLayout(): LayoutManager? = contentPanel?.layout

    override fun setLayout(mgr: LayoutManager?) {
        contentPanel?.layout = mgr
    }

    override fun add(comp: Component?): Component = contentPanel.add(comp)

    override fun add(comp: Component, constraints: Any?) {
        contentPanel.add(comp, constraints)
    }

    override fun add(comp: Component?, index: Int): Component = contentPanel.add(comp, index)

    override fun add(comp: Component?, constraints: Any?, index: Int) {
        contentPanel.add(comp, constraints, index)
    }

    override fun add(name: String?, comp: Component?): Component = contentPanel.add(name, comp)
}
