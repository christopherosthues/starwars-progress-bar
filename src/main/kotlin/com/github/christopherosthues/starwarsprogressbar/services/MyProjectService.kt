package com.github.christopherosthues.starwarsprogressbar.services

import com.intellij.openapi.project.Project
import com.github.christopherosthues.starwarsprogressbar.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
