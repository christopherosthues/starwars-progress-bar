package com.christopherosthues.starwarsprogressbar.exceptions

class MissingFileException(path: String) : Exception("File does not exist $path")
