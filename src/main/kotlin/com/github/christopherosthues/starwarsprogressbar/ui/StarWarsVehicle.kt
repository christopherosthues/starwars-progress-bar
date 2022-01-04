package com.github.christopherosthues.starwarsprogressbar.ui

import com.github.christopherosthues.starwarsprogressbar.StarWarsBundle.message
import java.awt.Color
import com.github.christopherosthues.starwarsprogressbar.BundleConstants
import com.intellij.util.containers.stream
import java.util.stream.Collectors

internal enum class StarWarsVehicle(val fileName: String, val vehicleName: String, val faction: Faction, val color: Color, val xShift: Int, val yShift: Int, val velocity: Float = 1f, val acceleration: Float = 1f) {
    ACCLAMATOR_CLASS_ASSAULT_SHIP("acclamator_class_assault_ship", message(BundleConstants.ACCLAMATOR_CLASS_ASSAULT_SHIP), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -7, -6),
    ARC_170_STARFIGHTER("arc_170_starfighter", message(BundleConstants.ARC_170_STARFIGHTER), Faction.GALACTIC_REPUBLIC, IonEngineColor.RedEngine, -12, -6),
    AZURE_ANGEL("azure_angel", message(BundleConstants.AZURE_ANGEL), Faction.GALACTIC_REPUBLIC, IonEngineColor.RedEngine, -10, -6),
    BLADE_OF_DORIN("blade_of_dorin", message(BundleConstants.BLADE_OF_DORIN), Faction.GALACTIC_REPUBLIC, IonEngineColor.RedEngine, -6, -6),
    DC0052_SPEEDER("dc0052_speeder", message(BundleConstants.DC0052_SPEEDER), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -13, -6),
    DELTA_7_JEDI_STARFIGHTER("delta_7_jedi_starfighter", message(BundleConstants.DELTA_7_JEDI_STARFIGHTER), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -4, -6),
    ETA_2_ACTIS_INTERCEPTOR("eta_2_actis", message(BundleConstants.ETA_2_ACTIS_INTERCEPTOR), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -6, -6),
    H_TYPE_NABOO_YACHT("h_type_naboo_yacht", message(BundleConstants.H_TYPE_NABOO_YACHT), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -4, -6),
    J_TYPE_DIPLOMATIC_BARGE("j_type_diplomatic_barge", message(BundleConstants.J_TYPE_DIPLOMATIC_BARGE), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -14, -6),
    J_TYPE_NABOO_STAR_SKIFF("j_type_naboo_star_skiff", message(BundleConstants.J_TYPE_NABOO_STAR_SKIFF), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -14, -6),
    N_1_STARFIGHTER("n_1_starfighter", message(BundleConstants.N_1_STARFIGHTER), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -16, -6),
    NABOO_ROYAL_STARSHIP("naboo_royal_starship", message(BundleConstants.NABOO_ROYAL_STARSHIP), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -6, -6),
    V_WING("v_wing", message(BundleConstants.V_WING), Faction.GALACTIC_REPUBLIC, IonEngineColor.YellowEngine, -4, -6),
    VENATOR_CLASS_STAR_DESTROYER("venator_class_star_destroyer", message(BundleConstants.VENATOR_CLASS_STAR_DESTROYER), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, 0, -6),
    V_19_TORRENT_STARFIGHTER("v_19_torrent_starfighter", message(BundleConstants.V_19_TORRENT_STARFIGHTER), Faction.GALACTIC_REPUBLIC, IonEngineColor.BlueEngine, -10, -6),
    Z_95_HEADHUNTER("z_95_headhunter", message(BundleConstants.Z_95_HEADHUNTER), Faction.GALACTIC_REPUBLIC, IonEngineColor.RedEngine, -9, -6),

    DIAMOND_CLASS_CRUISER("diamond_class_cruiser", message(BundleConstants.DIAMOND_CLASS_CRUISER), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -12, -6),
    DROID_TRI_FIGHTER("droid_tri_fighter", message(BundleConstants.DROID_TRI_FIGHTER), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -3, -6),
    FANBLADE("fanblade", message(BundleConstants.FANBLADE), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -12, -6),
    INVISIBLE_HAND("invisible_hand", message(BundleConstants.INVISIBLE_HAND), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.BlueEngine, 0, -6),
    LUCREHULK_CLASS_BATTLESHIP("droid_control_ship", message(BundleConstants.LUCREHULK_CLASS_BATTLESHIP), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.BlueEngine, -6, -6),
    MANKVIM_814_INTERCEPTOR("mankvim_814_interceptor", message(BundleConstants.MANKVIM_814_INTERCEPTOR), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.BlueEngine, -6, -6),
    MTT("multi_troop_transport", message(BundleConstants.MTT), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, 0, -6),
    MUNIFICENT_CLASS_STAR_FRIGATE("munificent_class_star_frigate", message(BundleConstants.MUNIFICENT_CLASS_STAR_FRIGATE), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -3, -6),
    NANTEX_CLASS_STARFIGHTER("nantex_class_starfighter", message(BundleConstants.NANTEX_CLASS_STARFIGHTER), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.BlueEngine, 0, -6),
    PLATOON_ATTACK_CRAFT("platoon_attack_craft", message(BundleConstants.PLATOON_ATTACK_CRAFT), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.BlueEngine, 0, -6),
    PORAX_38_STARFIGHTER("porax_38_starfighter", message(BundleConstants.PORAX_38_STARFIGHTER), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -10, -6),
    RECUSANT_CLASS_DESTROYER("recusant_class_destroyer", message(BundleConstants.RECUSANT_CLASS_DESTROYER), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -2, -6),
    SEPARATIST_SUPPLY_SHIP("separatist_supply_ship", message(BundleConstants.SEPARATIST_SUPPLY_SHIP), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -10, -6),
    SOULLESS_ONE("soulless_one", message(BundleConstants.SOULLESS_ONE), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -12, -6),
    STAP("stap", message(BundleConstants.STAP), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.YellowEngine, -12, -6),
    TRADE_FEDERATION_LANDING_SHIP("trade_federation_landing_ship", message(BundleConstants.TRADE_FEDERATION_LANDING_SHIP), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.RedEngine, -12, -6),
    VULTURE_DROID("vulture_droid", message(BundleConstants.VULTURE_DROID), Faction.CONFEDERACY_OF_INDEPENDENT_SYSTEMS, IonEngineColor.RedEngine, -12, -6),

    A_WING("a_wing", message(BundleConstants.A_WING), Faction.REBEL_ALLIANCE, IonEngineColor.RedEngine, -4, -6),
    B_WING("b_wing", message(BundleConstants.B_WING), Faction.REBEL_ALLIANCE, IonEngineColor.RedEngine, -13, -6),
    GR_75_TRANSPORT("gr_75_transport", message(BundleConstants.GR_75_TRANSPORT), Faction.REBEL_ALLIANCE, IonEngineColor.BlueEngine, 0, -6),
    HOME_ONE("home_one", message(BundleConstants.HOME_ONE), Faction.REBEL_ALLIANCE, IonEngineColor.BlueEngine, -2, -6),
    MC80_LIBERTY_STAR_CRUISER("mc80_liberty_star_cruiser", message(BundleConstants.MC80_LIBERTY_STAR_CRUISER), Faction.REBEL_ALLIANCE, IonEngineColor.BlueEngine, -9, -6),
    MILLENNIUM_FALCON("millennium_falcon", message(BundleConstants.MILLENNIUM_FALCON), Faction.REBEL_ALLIANCE, IonEngineColor.BlueEngine, -16, -6),
    NEBULON_B_FRIGATE("nebulon_b_frigate", message(BundleConstants.NEBULON_B_FRIGATE), Faction.REBEL_ALLIANCE, IonEngineColor.BlueEngine, -1, -6),
    ROGUE_SHADOW("rogue_shadow", message(BundleConstants.ROGUE_SHADOW), Faction.REBEL_ALLIANCE, IonEngineColor.BlueEngine, -8, -6),
    TANTIVE_IV("tantive_iv", message(BundleConstants.TANTIVE_IV), Faction.REBEL_ALLIANCE, IonEngineColor.RedEngine, 0, -6),
    T_47_SNOWSPEEDER("t_47_snowspeeder", message(BundleConstants.T_47_SNOWSPEEDER), Faction.REBEL_ALLIANCE, IonEngineColor.WhiteEngine, -9, -6),
    X_WING("x_wing", message(BundleConstants.X_WING), Faction.REBEL_ALLIANCE, IonEngineColor.RedEngine, -4, -6),
    Y_WING("y_wing", message(BundleConstants.Y_WING), Faction.REBEL_ALLIANCE, IonEngineColor.RedEngine, -2, -6),

    ECLIPSE("eclipse", message(BundleConstants.ECLIPSE), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -2, -6),
    EXECUTOR("executor", message(BundleConstants.EXECUTOR), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -8, -6),
    FIRST_DEATH_STAR("1st_death_star", message(BundleConstants.FIRST_DEATH_STAR), Faction.GALACTIC_EMPIRE, IonEngineColor.GreenEngine, -12, -6),
    INTERDICTOR_CLASS_STAR_DESTROYER("interdictor_class_star_destroyer", message(BundleConstants.INTERDICTOR_CLASS_STAR_DESTROYER), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -5, -6),
    LAMBDA_CLASS_SHUTTLE("lambda_class_shuttle", message(BundleConstants.LAMBDA_CLASS_SHUTTLE), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -13, -6),
    SECOND_DEATH_STAR("2nd_death_star", message(BundleConstants.SECOND_DEATH_STAR), Faction.GALACTIC_EMPIRE, IonEngineColor.GreenEngine, -12, -6),
    SENTINEL_CLASS_SHUTTLE("sentinel_class_shuttle", message(BundleConstants.SENTINEL_CLASS_SHUTTLE), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -8, -6),
    STAR_DESTROYER("star_destroyer", message(BundleConstants.STAR_DESTROYER), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -1, -6),
    TARTAN_CLASS_PATROL_CRUISER("tartan_class_patrol_cruiser", message(BundleConstants.TARTAN_CLASS_PATROL_CRUISER), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, 0, -6),
    THETA_CLASS_SHUTTLE("theta_class_shuttle", message(BundleConstants.THETA_CLASS_SHUTTLE), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -12, -6),
    TIE_BOMBER("tie_bomber", message(BundleConstants.TIE_BOMBER), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -8, -6),
    TIE_CRAWLER("tie_crawler", message(BundleConstants.TIE_CRAWLER), Faction.GALACTIC_EMPIRE, IonEngineColor.BrownEngine, -8, -6),
    TIE_FIGHTER("tie_fighter", message(BundleConstants.TIE_FIGHTER), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -16, -6),
    TIE_INTERCEPTOR("tie_interceptor", message(BundleConstants.TIE_INTERCEPTOR), Faction.GALACTIC_EMPIRE, IonEngineColor.BlueEngine, -9, -6),

    BOBA_FETTS_SLAVE_ONE("boba_fetts_slave_one", message(BundleConstants.BOBA_FETTS_SLAVE_ONE), Faction.BOUNTY_HUNTERS, IonEngineColor.RedEngine, -12, -6),
    IG_2000("ig_2000", message(BundleConstants.IG_2000), Faction.BOUNTY_HUNTERS, IonEngineColor.BlueEngine, -8, -6),
    JANGO_FETTS_SLAVE_ONE("jango_fetts_slave_one", message(BundleConstants.JANGO_FETTS_SLAVE_ONE), Faction.BOUNTY_HUNTERS, IonEngineColor.RedEngine, -12, -6),

    CLOUD_CITY("cloud_city", message(BundleConstants.CLOUD_CITY), Faction.SMUGGLERS, IonEngineColor.OrangeEngine, -16, -6),

    ANAKIN_SKYWALKERS_PODRACER("anakin_skywalkers_podracer", message(BundleConstants.ANAKIN_SKYWALKERS_PODRACER), Faction.PODRACER, IonEngineColor.PurpleEngine, -14, -6),
    SEBULBAS_PODRACER("sebulbas_podracer", message(BundleConstants.SEBULBAS_PODRACER), Faction.PODRACER, IonEngineColor.PurpleEngine, -14, -6),

    MISSING("missing", message(BundleConstants.MISSING), Faction.NONE, IonEngineColor.GreenEngine, -4, -6);

    companion object {
        @JvmField
        var DEFAULT_VEHICLES : List<StarWarsVehicle> = values().stream().filter { v: StarWarsVehicle -> v != MISSING }.collect(Collectors.toList())
    }
}
