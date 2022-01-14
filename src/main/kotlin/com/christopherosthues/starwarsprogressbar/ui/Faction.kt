//package com.christopherosthues.starwarsprogressbar.ui
//
//import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
//import com.christopherosthues.starwarsprogressbar.StarWarsBundle
//import com.intellij.util.containers.stream
//import java.util.stream.Collectors
//
//internal enum class Faction(val factionName: String) {
//    GALACTIC_REPUBLIC(StarWarsBundle.message(BundleConstants.GALACTIC_REPUBLIC)),
//    CONFEDERACY_OF_INDEPENDENT_SYSTEMS(StarWarsBundle.message(BundleConstants.CONFEDERACY_OF_INDEPENDENT_SYSTEMS)),
//    GALACTIC_EMPIRE(StarWarsBundle.message(BundleConstants.GALACTIC_EMPIRE)),
//    REBEL_ALLIANCE(StarWarsBundle.message(BundleConstants.REBEL_ALLIANCE)),
//    BOUNTY_HUNTERS(StarWarsBundle.message(BundleConstants.BOUNTY_HUNTERS)),
//    SMUGGLERS(StarWarsBundle.message(BundleConstants.SMUGGLERS)),
//    PODRACER(StarWarsBundle.message(BundleConstants.PODRACER)),
//
//    NONE("");
//
//    fun isValidFaction(): Boolean {
//        return this != NONE
//    }
//
//    companion object {
//        @JvmField
//        var DEFAULT_FACTIONS : List<Faction> =  values().stream().filter { faction : Faction -> faction.isValidFaction() }.collect(Collectors.toList())
//    }
//}
