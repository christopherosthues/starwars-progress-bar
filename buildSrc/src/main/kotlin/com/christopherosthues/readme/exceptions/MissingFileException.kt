package com.christopherosthues.readme.exceptions

class MissingFileException(path: String) : Exception("Readme file does not exist $path")
