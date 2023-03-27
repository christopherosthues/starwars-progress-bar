# starwars-progress-bar

![Build](https://github.com/christopherosthues/starwars-progress-bar/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/18483-star-wars-progress-bar.svg)](https://plugins.jetbrains.com/plugin/18483-star-wars-progress-bar/versions)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/18483-star-wars-progress-bar.svg)](https://plugins.jetbrains.com/plugin/18483-star-wars-progress-bar/reviews)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/18483-star-wars-progress-bar.svg)](https://plugins.jetbrains.com/plugin/18483-star-wars-progress-bar)

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

<!-- Plugin description -->
This is the Star Wars Progress Bar for IJ based IDEs. It replaces the progress bar with random vehicles from Star Wars.

<h3>Features</h3>
<ul>
  <li>Support of over 150 vehicles from the Star Wars movies, TV series, books and comics from the canon and legends</li>
  <li>Configurable via the settings (<kbd>Settings/Preferences</kbd> > <kbd>Appearance &amp; Behavior</kbd> > <kbd>Star Wars Progress Bar</kbd>)</li>
  <ul>
    <li>Selection of specific vehicles</li>
    <li>Selection of entire factions </li>
    <li>Show/hide vehicle in progress bar</li>
    <li>Display vehicle name in progress bar tooltip</li>
    <li>Display vehicle name in progress bar</li>
    <li>Display faction crest in progress bar</li>
    <li>Display solid or semi-transparent progress bar</li>
    <li>Display only the silhouette of the vehicle in progress bar</li>
    <li>Automatically change vehicle after a custom defined number of passes for indeterminate progress bars</li>
    <li>Different velocities for different vehicles</li>
    <li>Preview vehicles by clicking on their icon</li>
  </ul>
</ul>

<h3>Additional information</h3>
I do not own the Star Wars trademark nor do I intend any copyright infringement regarding the Star Wars trademark. This plugin was created for fun and is meant to be a homage to Star Wars and its creators, contributors and awesome fans.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Star Wars Progress Bar"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/christopherosthues/starwars-progress-bar/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---

## Vehicles

A list of all included vehicles can be found [here](LIST_OF_VEHICLES.md).

## Code

### Build scripts

The project provides a separate gradle task 'includeVehicles' which traverses the factions.json file in the src/main/resources/json directory. This file contains all vehicles with their properties. The gradle task reads this file in and generates a section for this README file that includes all vehicles provided by this plugin.

### Add new vehicles

To add new vehicles to this plugin you need to add a new entry to the [factions.json](src/main/resources/json/factions.json) which specifies the file name of the icon, the color of the progress bar, the x shift, the y shift and the velocity. Furthermore you need to provide translations for the vehicle name. For this you have to add a new entry to each StarWarsBundle(_locale).properties file. The localization key has to be prefixed with 'vehicles.' and should end with the name of the icon file.

For each vehicle you have to provide 4 icons two with a resolution size of 32x32 pixel and two with a resolution of 64x64 pixel. One icon of each resolution is used for the forward direction and one for the backward direction. Note that you can only use the middle vertical 32 pixel of the 64x64 respectivly the middle verticle 16 pixels of the 32x32 icons otherwise the progressbar will cut of everything beyond it. The plugin uses only the 32x32 resolution icons but the 64x64 are referenced by this README.

The naming of the icons should as followed:

* vehicle_name.png (for the forward 32x32 pixel icon)
* vehicle_name_r.png (for the backward 32x32 pixel icon)
* vehicle_name@2x.png (for the forward 64x64 pixel icon)
* vehicle_name_r@2x.png (for the backward 64x64 pixel icon)

The vehicle name should not contain any spaces or dashes. Please use underscores instead.

## Acknowledgements

### Code

* Plugin based on the [IntelliJ Platform Plugin Template][template].
* The code is based on and adapted from the work of Dmitry Batkovich's [Nyan Progess Bar](https://github.com/batya239/NyanProgressBar) and Karl Goffin's [Pokemon Progress Bar](https://github.com/kagof/intellij-pokemon-progress).

### Icons

* All icons were created by [me](https://github.com/christopherosthues).

### Misc

* Special thanks to George Lucas and Lucasfilm for creating Star Wars
