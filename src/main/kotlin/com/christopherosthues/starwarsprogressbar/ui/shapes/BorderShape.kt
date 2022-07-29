package com.christopherosthues.starwarsprogressbar.ui.shapes

class BorderShape(var x: Int, var y: Int, var width: Int, var height: Int, margin: Int, topInset: Int) {
    init {
        x += margin
        y += margin
        width -= 2 * margin
        height -= 2 * margin
        if (topInset < margin) {
            height += topInset
            y -= topInset
        }
    }
}
