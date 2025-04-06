package com.christopherosthues.starwarsprogressbar.ui.events

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity

internal interface StarWarsEntityClickListener {
    fun starWarsEntityClicked(starWarsEntity: StarWarsEntity)
}
