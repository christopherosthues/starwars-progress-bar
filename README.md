# starwars-progress-bar

![Build](https://github.com/christopherosthues/starwars-progress-bar/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/18483-star-wars-progress-bar.svg)](https://plugins.jetbrains.com/plugin/18483-star-wars-progress-bar/versions)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/18483-star-wars-progress-bar.svg)](https://plugins.jetbrains.com/plugin/18483-star-wars-progress-bar/reviews)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/18483-star-wars-progress-bar.svg)](https://plugins.jetbrains.com/plugin/18483-star-wars-progress-bar)

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[//]: # (## Template ToDo list`)

[//]: # (- [x] Create a new [IntelliJ Platform Plugin Template][template] project.)

[//]: # (- [x] Get familiar with the [template documentation][template].)

[//]: # (- [x] Verify the [pluginGroup]&#40;./gradle.properties&#41;, [plugin ID]&#40;./src/main/resources/META-INF/plugin.xml&#41; and [sources package]&#40;./src/main/kotlin&#41;.)

[//]: # (- [x] Review the [Legal Agreements]&#40;https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate&#41;.)

[//]: # (- [x] [Publish a plugin manually]&#40;https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate&#41; for the first time.)

[//]: # (- [x] Set the Plugin ID in the above README badges.)

[//]: # (- [x] Set the [Plugin Signing]&#40;https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate&#41; related [secrets]&#40;https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables&#41;.)

[//]: # (- [x] Set the [Deployment Token]&#40;https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate&#41;.)

[//]: # (- [x] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about rel`eases containing new features and fixes.)

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

<!-- Included vehicles -->

[//]: # (## Included vehicles)

[//]: # ()
[//]: # (### Trade Federation)

[//]: # ()
[//]: # (* ![Armored Assault Tank Mk I]&#40;icons/trade_federation/armored_assault_tank@2x.png&#41; Armored Assault Tank Mk I ![Armored Assault Tank Mk I]&#40;icons/trade_federation/armored_assault_tank_r@2x.png&#41;)

[//]: # (* ![Lucrehulk-class Droid control ship]&#40;icons/trade_federation/lucrehulk_class_droid_control_ship@2x.png&#41; Lucrehulk-class Droid control ship ![Lucrehulk-class Droid control ship]&#40;icons/trade_federation/lucrehulk_class_droid_control_ship_r@2x.png&#41;)

[//]: # (* ![Multi-Troop Transport]&#40;icons/trade_federation/multi_troop_transport@2x.png&#41; Multi-Troop Transport ![Multi-Troop Transport]&#40;icons/trade_federation/multi_troop_transport_r@2x.png&#41;)

[//]: # (* ![Platoon Attack Craft]&#40;icons/trade_federation/platoon_attack_craft@2x.png&#41; Platoon Attack Craft ![Platoon Attack Craft]&#40;icons/trade_federation/platoon_attack_craft_r@2x.png&#41;)

[//]: # (* ![Sheathipede-class tranport shuttle]&#40;icons/trade_federation/sheathipede_class_shuttle@2x.png&#41; Sheathipede-class tranport shuttle ![Sheathipede-class tranport shuttle]&#40;icons/trade_federation/sheathipede_class_shuttle_r@2x.png&#41;)

[//]: # (* ![STAP]&#40;icons/trade_federation/stap@2x.png&#41; STAP ![STAP]&#40;icons/trade_federation/stap_r@2x.png&#41;)

[//]: # (* ![Trade Federation Landing Ship]&#40;icons/trade_federation/trade_federation_landing_ship@2x.png&#41; Trade Federation Landing Ship ![Trade Federation Landing Ship]&#40;icons/trade_federation/trade_federation_landing_ship_r@2x.png&#41;)

[//]: # (* ![Vulture Droid]&#40;icons/trade_federation/vulture_droid@2x.png&#41; Vulture Droid ![Vulture Droid]&#40;icons/trade_federation/vulture_droid_r@2x.png&#41;)

[//]: # ()
[//]: # (### Jedi Order)

[//]: # ()
[//]: # (* ![Adi Gallia's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/adi_gallias_delta_7b_jedi_starfighter@2x.png&#41; Adi Gallia's Delta-7b-Aethersprite Starfighter ![Adi Gallia's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/adi_gallias_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Ahsoka Tano's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/ahsoka_tanos_delta_7b_jedi_starfighter@2x.png&#41; Ahsoka Tano's Delta-7b-Aethersprite Starfighter ![Ahsoka Tano's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/ahsoka_tanos_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Ahsoka Tano's Eta-2 Actis Interceptor]&#40;icons/jedi/ahsoka_tanos_eta_2_actis_interceptor@2x.png&#41; Ahsoka Tano's Eta-2 Actis Interceptor ![Ahsoka Tano's Eta-2 Actis Interceptor]&#40;icons/jedi/ahsoka_tanos_eta_2_actis_interceptor_r@2x.png&#41;)

[//]: # (* ![Anakin Skywalker's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/anakin_skywalkers_delta_7b_jedi_starfighter@2x.png&#41; Anakin Skywalker's Delta-7b-Aethersprite Starfighter ![Anakin Skywalker's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/anakin_skywalkers_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Anakin Skywalker's Eta-2 Actis Interceptor]&#40;icons/jedi/anakin_skywalkers_eta_2_actis_interceptor@2x.png&#41; Anakin Skywalker's Eta-2 Actis Interceptor ![Anakin Skywalker's Eta-2 Actis Interceptor]&#40;icons/jedi/anakin_skywalkers_eta_2_actis_interceptor_r@2x.png&#41;)

[//]: # (* ![Azure Angel]&#40;icons/jedi/azure_angel@2x.png&#41; Azure Angel ![Azure Angel]&#40;icons/jedi/azure_angel_r@2x.png&#41;)

[//]: # (* ![Barriss Offee's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/barriss_offees_delta_7b_jedi_starfighter@2x.png&#41; Barriss Offee's Delta-7b-Aethersprite Starfighter ![Barriss Offee's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/barriss_offees_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Blade of Dorin]&#40;icons/jedi/blade_of_dorin@2x.png&#41; Blade of Dorin ![Blade of Dorin]&#40;icons/jedi/blade_of_dorin_r@2x.png&#41;)

[//]: # (* ![DC0052 Speeder]&#40;icons/jedi/dc0052_speeder@2x.png&#41; DC0052 Speeder ![DC0052 Speeder]&#40;icons/jedi/dc0052_speeder_r@2x.png&#41;)

[//]: # (* ![Eta-class Shuttle]&#40;icons/jedi/eta_class_shuttle@2x.png&#41; Eta-class Shuttle ![Eta-class Shuttle]&#40;icons/jedi/eta_class_shuttle_r@2x.png&#41;)

[//]: # (* ![Kit Fisto's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/kit_fistos_delta_7b_jedi_starfighter@2x.png&#41; Kit Fisto's Delta-7b-Aethersprite Starfighter ![Kit Fisto's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/kit_fistos_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Luminara Unduli's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/luminara_undulis_delta_7b_jedi_starfighter@2x.png&#41; Luminara Unduli's Delta-7b-Aethersprite Starfighter ![Luminara Unduli's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/luminara_undulis_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Mace Windu's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/mace_windus_delta_7b_jedi_starfighter@2x.png&#41; Mace Windu's Delta-7b-Aethersprite Starfighter ![Mace Windu's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/mace_windus_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Obi-Wan Kenobi's Delta-7-Aethersprite Starfighter]&#40;icons/jedi/obi_wan_kenobis_delta_7_jedi_starfighter@2x.png&#41; Obi-Wan Kenobi's Delta-7-Aethersprite Starfighter ![Obi-Wan Kenobi's Delta-7-Aethersprite Starfighter]&#40;icons/jedi/obi_wan_kenobis_delta_7_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Obi-Wan Kenobi's Eta-2 Actis Interceptor]&#40;icons/jedi/obi_wan_kenobis_eta_2_actis_interceptor@2x.png&#41; Obi-Wan Kenobi's Eta-2 Actis Interceptor ![Obi-Wan Kenobi's Eta-2 Actis Interceptor]&#40;icons/jedi/obi_wan_kenobis_eta_2_actis_interceptor_r@2x.png&#41;)

[//]: # (* ![Praxis Mk I turbo speeder]&#40;icons/jedi/praxis_mk_i_turbo_speeder@2x.png&#41; Praxis Mk I turbo speeder ![Praxis Mk I turbo speeder]&#40;icons/jedi/praxis_mk_i_turbo_speeder_r@2x.png&#41;)

[//]: # (* ![Saesee Tiin's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/saesee_tiins_delta_7b_jedi_starfighter@2x.png&#41; Saesee Tiin's Delta-7b-Aethersprite Starfighter ![Saesee Tiin's Delta-7b-Aethersprite Starfighter]&#40;icons/jedi/saesee_tiins_delta_7b_jedi_starfighter_r@2x.png&#41;)

[//]: # (* ![Stinger Mantis]&#40;icons/jedi/stinger_mantis@2x.png&#41; Stinger Mantis ![Stinger Mantis]&#40;icons/jedi/stinger_mantis_r@2x.png&#41;)

[//]: # ()
[//]: # (### Galactic Republic)

[//]: # ()
[//]: # (* ![AA-9 Coruscant Freighter]&#40;icons/galactic_republic/aa_9_coruscant_freighter@2x.png&#41; AA-9 Coruscant Freighter ![AA-9 Coruscant Freighter]&#40;icons/galactic_republic/aa_9_coruscant_freighter_r@2x.png&#41;)

[//]: # (* ![Acclamator-class Assault Ship]&#40;icons/galactic_republic/acclamator_class_assault_ship@2x.png&#41; Acclamator-class Assault Ship ![Acclamator-class Assault Ship]&#40;icons/galactic_republic/acclamator_class_assault_ship_r@2x.png&#41;)

[//]: # (* ![Acclamator-II-class Assault Ship]&#40;icons/galactic_republic/acclamator_ii_class_assault_ship@2x.png&#41; Acclamator-II-class Assault Ship ![Acclamator-II-class Assault Ship]&#40;icons/galactic_republic/acclamator_ii_class_assault_ship_r@2x.png&#41;)

[//]: # (* ![ARC-170 Starfighter]&#40;icons/galactic_republic/arc_170_starfighter@2x.png&#41; ARC-170 Starfighter ![ARC-170 Starfighter]&#40;icons/galactic_republic/arc_170_starfighter_r@2x.png&#41;)

[//]: # (* ![Arquitens-class light cruiser]&#40;icons/galactic_republic/arquitens_class_light_cruiser@2x.png&#41; Arquitens-class light cruiser ![Arquitens-class light cruiser]&#40;icons/galactic_republic/arquitens_class_light_cruiser_r@2x.png&#41;)

[//]: # (* ![AT-AP]&#40;icons/galactic_republic/at_ap@2x.png&#41; AT-AP ![AT-AP]&#40;icons/galactic_republic/at_ap_r@2x.png&#41;)

[//]: # (* ![Berenko-class gondola speeder]&#40;icons/galactic_republic/berenko_class_gondola_speeder@2x.png&#41; Berenko-class gondola speeder ![Berenko-class gondola speeder]&#40;icons/galactic_republic/berenko_class_gondola_speeder_r@2x.png&#41;)

[//]: # (* ![BTL-B Y-Wing]&#40;icons/galactic_republic/btl_b_y_wing@2x.png&#41; BTL-B Y-Wing ![BTL-B Y-Wing]&#40;icons/galactic_republic/btl_b_y_wing_r@2x.png&#41;)

[//]: # (* ![Captain Rex's BTL-B Y-Wing]&#40;icons/galactic_republic/captain_rex_y_wing@2x.png&#41; Captain Rex's BTL-B Y-Wing ![Captain Rex's BTL-B Y-Wing]&#40;icons/galactic_republic/captain_rex_y_wing_r@2x.png&#41;)

[//]: # (* ![CSS-1 Corellian Star Shuttle]&#40;icons/galactic_republic/css_1_corellian_star_shuttle@2x.png&#41; CSS-1 Corellian Star Shuttle ![CSS-1 Corellian Star Shuttle]&#40;icons/galactic_republic/css_1_corellian_star_shuttle_r@2x.png&#41;)

[//]: # (* ![H-Type Naboo Yacht]&#40;icons/galactic_republic/h_type_naboo_yacht@2x.png&#41; H-Type Naboo Yacht ![H-Type Naboo Yacht]&#40;icons/galactic_republic/h_type_naboo_yacht_r@2x.png&#41;)

[//]: # (* ![Haven-class medical station]&#40;icons/galactic_republic/haven_class_medical_station@2x.png&#41; Haven-class medical station ![Haven-class medical station]&#40;icons/galactic_republic/haven_class_medical_station_r@2x.png&#41;)

[//]: # (* ![Heavy Assault Vehicle/Wheeled A6 Juggernaut]&#40;icons/galactic_republic/havw_a6_juggernaut@2x.png&#41; Heavy Assault Vehicle/Wheeled A6 Juggernaut ![Heavy Assault Vehicle/Wheeled A6 Juggernaut]&#40;icons/galactic_republic/havw_a6_juggernaut_r@2x.png&#41;)

[//]: # (* ![High-Altitude Entry Transport-221]&#40;icons/galactic_republic/high_altitude_entry_transport_221@2x.png&#41; High-Altitude Entry Transport-221 ![High-Altitude Entry Transport-221]&#40;icons/galactic_republic/high_altitude_entry_transport_221_r@2x.png&#41;)

[//]: # (* ![Infantry Support Platform]&#40;icons/galactic_republic/infantry_support_platform@2x.png&#41; Infantry Support Platform ![Infantry Support Platform]&#40;icons/galactic_republic/infantry_support_platform_r@2x.png&#41;)

[//]: # (* ![IPV-2C Stealth Corvette]&#40;icons/galactic_republic/ipv_2c_stealth_corvette@2x.png&#41; IPV-2C Stealth Corvette ![IPV-2C Stealth Corvette]&#40;icons/galactic_republic/ipv_2c_stealth_corvette_r@2x.png&#41;)

[//]: # (* ![J-Type Diplomatic Barge]&#40;icons/galactic_republic/j_type_diplomatic_barge@2x.png&#41; J-Type Diplomatic Barge ![J-Type Diplomatic Barge]&#40;icons/galactic_republic/j_type_diplomatic_barge_r@2x.png&#41;)

[//]: # (* ![J-Type Naboo Star Skiff]&#40;icons/galactic_republic/j_type_naboo_star_skiff@2x.png&#41; J-Type Naboo Star Skiff ![J-Type Naboo Star Skiff]&#40;icons/galactic_republic/j_type_naboo_star_skiff_r@2x.png&#41;)

[//]: # (* ![LAAT/i]&#40;icons/galactic_republic/laat_i@2x.png&#41; LAAT/i ![LAAT/i]&#40;icons/galactic_republic/laat_i_r@2x.png&#41;)

[//]: # (* ![N-1 Starfighter]&#40;icons/galactic_republic/n_1_starfighter@2x.png&#41; N-1 Starfighter ![N-1 Starfighter]&#40;icons/galactic_republic/n_1_starfighter_r@2x.png&#41;)

[//]: # (* ![Naboo Royal Starship]&#40;icons/galactic_republic/naboo_royal_starship@2x.png&#41; Naboo Royal Starship ![Naboo Royal Starship]&#40;icons/galactic_republic/naboo_royal_starship_r@2x.png&#41;)

[//]: # (* ![Oevvaor Jet Catamaran]&#40;icons/galactic_republic/oevvaor_jet_catamaran@2x.png&#41; Oevvaor Jet Catamaran ![Oevvaor Jet Catamaran]&#40;icons/galactic_republic/oevvaor_jet_catamaran_r@2x.png&#41;)

[//]: # (* ![Seraph-class Flash speeder]&#40;icons/galactic_republic/seraph_class_speeder@2x.png&#41; Seraph-class Flash speeder ![Seraph-class Flash speeder]&#40;icons/galactic_republic/seraph_class_speeder_r@2x.png&#41;)

[//]: # (* ![Syluire-31 hyperdrive docking ring]&#40;icons/galactic_republic/syluire_31_hyperspace_docking_ring@2x.png&#41; Syluire-31 hyperdrive docking ring ![Syluire-31 hyperdrive docking ring]&#40;icons/galactic_republic/syluire_31_hyperspace_docking_ring_r@2x.png&#41;)

[//]: # (* ![V-19 Torrent Starfighter]&#40;icons/galactic_republic/v_19_torrent_starfighter@2x.png&#41; V-19 Torrent Starfighter ![V-19 Torrent Starfighter]&#40;icons/galactic_republic/v_19_torrent_starfighter_r@2x.png&#41;)

[//]: # (* ![V-Wing]&#40;icons/galactic_republic/v_wing@2x.png&#41; V-Wing ![V-Wing]&#40;icons/galactic_republic/v_wing_r@2x.png&#41;)

[//]: # (* ![Venator-class Star Destroyer]&#40;icons/galactic_republic/venator_class_star_destroyer@2x.png&#41; Venator-class Star Destroyer ![Venator-class Star Destroyer]&#40;icons/galactic_republic/venator_class_star_destroyer_r@2x.png&#41;)

[//]: # (* ![XJ-6 Airspeeder]&#40;icons/galactic_republic/xj_6_airspeeder@2x.png&#41; XJ-6 Airspeeder ![XJ-6 Airspeeder]&#40;icons/galactic_republic/xj_6_airspeeder_r@2x.png&#41;)

[//]: # (* ![Z-95 Headhunter]&#40;icons/galactic_republic/z_95_headhunter@2x.png&#41; Z-95 Headhunter ![Z-95 Headhunter]&#40;icons/galactic_republic/z_95_headhunter_r@2x.png&#41;)

[//]: # ()
[//]: # (### Confederacy of independent systems)

[//]: # ()
[//]: # (* ![Armored Assault Tank Mk I]&#40;icons/confederacy_of_independent_systems/armored_assault_tank@2x.png&#41; Armored Assault Tank Mk I ![Armored Assault Tank Mk I]&#40;icons/confederacy_of_independent_systems/armored_assault_tank_r@2x.png&#41;)

[//]: # (* ![Bloodfin]&#40;icons/confederacy_of_independent_systems/bloodfin@2x.png&#41; Bloodfin ![Bloodfin]&#40;icons/confederacy_of_independent_systems/bloodfin_r@2x.png&#41;)

[//]: # (* ![Core ship]&#40;icons/confederacy_of_independent_systems/core_ship@2x.png&#41; Core ship ![Core ship]&#40;icons/confederacy_of_independent_systems/core_ship_r@2x.png&#41;)

[//]: # (* ![Count Dooku's Flitknot Speeder Bike]&#40;icons/confederacy_of_independent_systems/count_dookus_flitknot_speeder@2x.png&#41; Count Dooku's Flitknot Speeder Bike ![Count Dooku's Flitknot Speeder Bike]&#40;icons/confederacy_of_independent_systems/count_dookus_flitknot_speeder_r@2x.png&#41;)

[//]: # (* ![DH-Omni Support Vessel]&#40;icons/confederacy_of_independent_systems/separatist_supply_ship@2x.png&#41; DH-Omni Support Vessel ![DH-Omni Support Vessel]&#40;icons/confederacy_of_independent_systems/separatist_supply_ship_r@2x.png&#41;)

[//]: # (* ![Diamond-class Cruiser]&#40;icons/confederacy_of_independent_systems/diamond_class_cruiser@2x.png&#41; Diamond-class Cruiser ![Diamond-class Cruiser]&#40;icons/confederacy_of_independent_systems/diamond_class_cruiser_r@2x.png&#41;)

[//]: # (* ![Droch-class boarding ship]&#40;icons/confederacy_of_independent_systems/droch_class_boarding_ship@2x.png&#41; Droch-class boarding ship ![Droch-class boarding ship]&#40;icons/confederacy_of_independent_systems/droch_class_boarding_ship_r@2x.png&#41;)

[//]: # (* ![Droid Tri-Fighter]&#40;icons/confederacy_of_independent_systems/droid_tri_fighter@2x.png&#41; Droid Tri-Fighter ![Droid Tri-Fighter]&#40;icons/confederacy_of_independent_systems/droid_tri_fighter_r@2x.png&#41;)

[//]: # (* ![Fanblade]&#40;icons/confederacy_of_independent_systems/fanblade@2x.png&#41; Fanblade ![Fanblade]&#40;icons/confederacy_of_independent_systems/fanblade_r@2x.png&#41;)

[//]: # (* ![Flitknot Speeder Bike]&#40;icons/confederacy_of_independent_systems/flitknot_speeder@2x.png&#41; Flitknot Speeder Bike ![Flitknot Speeder Bike]&#40;icons/confederacy_of_independent_systems/flitknot_speeder_r@2x.png&#41;)

[//]: # (* ![Heavy Missile Platform droid gunship]&#40;icons/confederacy_of_independent_systems/heavy_missile_platform_droid_gunship@2x.png&#41; Heavy Missile Platform droid gunship ![Heavy Missile Platform droid gunship]&#40;icons/confederacy_of_independent_systems/heavy_missile_platform_droid_gunship_r@2x.png&#41;)

[//]: # (* ![IG-227 Hailfire class Droid tank]&#40;icons/confederacy_of_independent_systems/ig_227_hailfire_class_droid_tank@2x.png&#41; IG-227 Hailfire class Droid tank ![IG-227 Hailfire class Droid tank]&#40;icons/confederacy_of_independent_systems/ig_227_hailfire_class_droid_tank_r@2x.png&#41;)

[//]: # (* ![Invisible Hand]&#40;icons/confederacy_of_independent_systems/invisible_hand@2x.png&#41; Invisible Hand ![Invisible Hand]&#40;icons/confederacy_of_independent_systems/invisible_hand_r@2x.png&#41;)

[//]: # (* ![Landing Craft]&#40;icons/confederacy_of_independent_systems/landing_craft@2x.png&#41; Landing Craft ![Landing Craft]&#40;icons/confederacy_of_independent_systems/landing_craft_r@2x.png&#41;)

[//]: # (* ![Lucrehulk-class Battleship]&#40;icons/confederacy_of_independent_systems/lucrehulk_class_battleship@2x.png&#41; Lucrehulk-class Battleship ![Lucrehulk-class Battleship]&#40;icons/confederacy_of_independent_systems/lucrehulk_class_battleship_r@2x.png&#41;)

[//]: # (* ![Malevolence]&#40;icons/confederacy_of_independent_systems/malevolence@2x.png&#41; Malevolence ![Malevolence]&#40;icons/confederacy_of_independent_systems/malevolence_r@2x.png&#41;)

[//]: # (* ![Mankvim-814 Interceptor]&#40;icons/confederacy_of_independent_systems/mankvim_814_interceptor@2x.png&#41; Mankvim-814 Interceptor ![Mankvim-814 Interceptor]&#40;icons/confederacy_of_independent_systems/mankvim_814_interceptor_r@2x.png&#41;)

[//]: # (* ![Maxillipede-class Shuttle]&#40;icons/confederacy_of_independent_systems/maxillipede_shuttle@2x.png&#41; Maxillipede-class Shuttle ![Maxillipede-class Shuttle]&#40;icons/confederacy_of_independent_systems/maxillipede_shuttle_r@2x.png&#41;)

[//]: # (* ![Multi-Troop Transport]&#40;icons/confederacy_of_independent_systems/multi_troop_transport@2x.png&#41; Multi-Troop Transport ![Multi-Troop Transport]&#40;icons/confederacy_of_independent_systems/multi_troop_transport_r@2x.png&#41;)

[//]: # (* ![Munificent-class Star Frigate]&#40;icons/confederacy_of_independent_systems/munificent_class_star_frigate@2x.png&#41; Munificent-class Star Frigate ![Munificent-class Star Frigate]&#40;icons/confederacy_of_independent_systems/munificent_class_star_frigate_r@2x.png&#41;)

[//]: # (* ![Nantex-class Starfighter]&#40;icons/confederacy_of_independent_systems/nantex_class_starfighter@2x.png&#41; Nantex-class Starfighter ![Nantex-class Starfighter]&#40;icons/confederacy_of_independent_systems/nantex_class_starfighter_r@2x.png&#41;)

[//]: # (* ![NR-N99 Persuader-class droid enforcer]&#40;icons/confederacy_of_independent_systems/nr_n99_persuader_class_droid_enforcer@2x.png&#41; NR-N99 Persuader-class droid enforcer ![NR-N99 Persuader-class droid enforcer]&#40;icons/confederacy_of_independent_systems/nr_n99_persuader_class_droid_enforcer_r@2x.png&#41;)

[//]: # (* ![Octuptarra tri-droid]&#40;icons/confederacy_of_independent_systems/octuptarra_tri_droid@2x.png&#41; Octuptarra tri-droid ![Octuptarra tri-droid]&#40;icons/confederacy_of_independent_systems/octuptarra_tri_droid_r@2x.png&#41;)

[//]: # (* ![OG-9 homing spider droid]&#40;icons/confederacy_of_independent_systems/og_9_homing_spider_droid@2x.png&#41; OG-9 homing spider droid ![OG-9 homing spider droid]&#40;icons/confederacy_of_independent_systems/og_9_homing_spider_droid_r@2x.png&#41;)

[//]: # (* ![Platoon Attack Craft]&#40;icons/confederacy_of_independent_systems/platoon_attack_craft@2x.png&#41; Platoon Attack Craft ![Platoon Attack Craft]&#40;icons/confederacy_of_independent_systems/platoon_attack_craft_r@2x.png&#41;)

[//]: # (* ![Porax-38 Starfighter]&#40;icons/confederacy_of_independent_systems/porax_38_starfighter@2x.png&#41; Porax-38 Starfighter ![Porax-38 Starfighter]&#40;icons/confederacy_of_independent_systems/porax_38_starfighter_r@2x.png&#41;)

[//]: # (* ![Punworcca 116-class interstellar sloop]&#40;icons/confederacy_of_independent_systems/punworcca_116_class_interstellar_sloop@2x.png&#41; Punworcca 116-class interstellar sloop ![Punworcca 116-class interstellar sloop]&#40;icons/confederacy_of_independent_systems/punworcca_116_class_interstellar_sloop_r@2x.png&#41;)

[//]: # (* ![Recusant-class Destroyer]&#40;icons/confederacy_of_independent_systems/recusant_class_destroyer@2x.png&#41; Recusant-class Destroyer ![Recusant-class Destroyer]&#40;icons/confederacy_of_independent_systems/recusant_class_destroyer_r@2x.png&#41;)

[//]: # (* ![Scimitar]&#40;icons/confederacy_of_independent_systems/scimitar@2x.png&#41; Scimitar ![Scimitar]&#40;icons/confederacy_of_independent_systems/scimitar_r@2x.png&#41;)

[//]: # (* ![Sheathipede-class tranport shuttle]&#40;icons/confederacy_of_independent_systems/sheathipede_class_shuttle@2x.png&#41; Sheathipede-class tranport shuttle ![Sheathipede-class tranport shuttle]&#40;icons/confederacy_of_independent_systems/sheathipede_class_shuttle_r@2x.png&#41;)

[//]: # (* ![Sheathipede-class Type B shuttle]&#40;icons/confederacy_of_independent_systems/sheathipede_class_type_b_shuttle@2x.png&#41; Sheathipede-class Type B shuttle ![Sheathipede-class Type B shuttle]&#40;icons/confederacy_of_independent_systems/sheathipede_class_type_b_shuttle_r@2x.png&#41;)

[//]: # (* ![Soulless One]&#40;icons/confederacy_of_independent_systems/soulless_one@2x.png&#41; Soulless One ![Soulless One]&#40;icons/confederacy_of_independent_systems/soulless_one_r@2x.png&#41;)

[//]: # (* ![STAP]&#40;icons/confederacy_of_independent_systems/stap@2x.png&#41; STAP ![STAP]&#40;icons/confederacy_of_independent_systems/stap_r@2x.png&#41;)

[//]: # (* ![Vulture Droid]&#40;icons/confederacy_of_independent_systems/vulture_droid@2x.png&#41; Vulture Droid ![Vulture Droid]&#40;icons/confederacy_of_independent_systems/vulture_droid_r@2x.png&#41;)

[//]: # ()
[//]: # (### Galactic Empire)

[//]: # ()
[//]: # (* ![Arquitens-class command cruiser]&#40;icons/galactic_empire/arquitens_class_command_cruiser@2x.png&#41; Arquitens-class command cruiser ![Arquitens-class command cruiser]&#40;icons/galactic_empire/arquitens_class_command_cruiser_r@2x.png&#41;)

[//]: # (* ![AT-ACT]&#40;icons/galactic_empire/at_act@2x.png&#41; AT-ACT ![AT-ACT]&#40;icons/galactic_empire/at_act_r@2x.png&#41;)

[//]: # (* ![AT-AT]&#40;icons/galactic_empire/at_at@2x.png&#41; AT-AT ![AT-AT]&#40;icons/galactic_empire/at_at_r@2x.png&#41;)

[//]: # (* ![AT-DP]&#40;icons/galactic_empire/at_dp@2x.png&#41; AT-DP ![AT-DP]&#40;icons/galactic_empire/at_dp_r@2x.png&#41;)

[//]: # (* ![AT-ST]&#40;icons/galactic_empire/at_st@2x.png&#41; AT-ST ![AT-ST]&#40;icons/galactic_empire/at_st_r@2x.png&#41;)

[//]: # (* ![Cantwell-class Arrestor Cruiser]&#40;icons/galactic_empire/cantwell_class_arrestor_cruiser@2x.png&#41; Cantwell-class Arrestor Cruiser ![Cantwell-class Arrestor Cruiser]&#40;icons/galactic_empire/cantwell_class_arrestor_cruiser_r@2x.png&#41;)

[//]: # (* ![Eclipse]&#40;icons/galactic_empire/eclipse@2x.png&#41; Eclipse ![Eclipse]&#40;icons/galactic_empire/eclipse_r@2x.png&#41;)

[//]: # (* ![Executor]&#40;icons/galactic_empire/executor@2x.png&#41; Executor ![Executor]&#40;icons/galactic_empire/executor_r@2x.png&#41;)

[//]: # (* ![First Death Star]&#40;icons/galactic_empire/first_death_star@2x.png&#41; First Death Star ![First Death Star]&#40;icons/galactic_empire/first_death_star_r@2x.png&#41;)

[//]: # (* ![Imperial II-class Star Destroyer]&#40;icons/galactic_empire/imperial_class_star_destroyer@2x.png&#41; Imperial II-class Star Destroyer ![Imperial II-class Star Destroyer]&#40;icons/galactic_empire/imperial_class_star_destroyer_r@2x.png&#41;)

[//]: # (* ![Interdictor-class Star Destroyer]&#40;icons/galactic_empire/interdictor_class_star_destroyer@2x.png&#41; Interdictor-class Star Destroyer ![Interdictor-class Star Destroyer]&#40;icons/galactic_empire/interdictor_class_star_destroyer_r@2x.png&#41;)

[//]: # (* ![Lambda-class Shuttle]&#40;icons/galactic_empire/lambda_class_shuttle@2x.png&#41; Lambda-class Shuttle ![Lambda-class Shuttle]&#40;icons/galactic_empire/lambda_class_shuttle_r@2x.png&#41;)

[//]: # (* ![Mining Guild TIE Fighter]&#40;icons/galactic_empire/mining_guild_tie_fighter@2x.png&#41; Mining Guild TIE Fighter ![Mining Guild TIE Fighter]&#40;icons/galactic_empire/mining_guild_tie_fighter_r@2x.png&#41;)

[//]: # (* ![Onager-class Star Destroyer]&#40;icons/galactic_empire/onager_class_star_destroyer@2x.png&#41; Onager-class Star Destroyer ![Onager-class Star Destroyer]&#40;icons/galactic_empire/onager_class_star_destroyer_r@2x.png&#41;)

[//]: # (* ![Quasar Fire-class cruiser carrier]&#40;icons/galactic_empire/quasar_fire_class_cruiser_carrier@2x.png&#41; Quasar Fire-class cruiser carrier ![Quasar Fire-class cruiser carrier]&#40;icons/galactic_empire/quasar_fire_class_cruiser_carrier_r@2x.png&#41;)

[//]: # (* ![Second Death Star]&#40;icons/galactic_empire/second_death_star@2x.png&#41; Second Death Star ![Second Death Star]&#40;icons/galactic_empire/second_death_star_r@2x.png&#41;)

[//]: # (* ![Sentinel-class Shuttle]&#40;icons/galactic_empire/sentinel_class_shuttle@2x.png&#41; Sentinel-class Shuttle ![Sentinel-class Shuttle]&#40;icons/galactic_empire/sentinel_class_shuttle_r@2x.png&#41;)

[//]: # (* ![Tartan-class patrol cruiser]&#40;icons/galactic_empire/tartan_class_patrol_cruiser@2x.png&#41; Tartan-class patrol cruiser ![Tartan-class patrol cruiser]&#40;icons/galactic_empire/tartan_class_patrol_cruiser_r@2x.png&#41;)

[//]: # (* ![Theta-class Shuttle]&#40;icons/galactic_empire/theta_class_shuttle@2x.png&#41; Theta-class Shuttle ![Theta-class Shuttle]&#40;icons/galactic_empire/theta_class_shuttle_r@2x.png&#41;)

[//]: # (* ![TIE Advanced x1]&#40;icons/galactic_empire/tie_advanced_x1@2x.png&#41; TIE Advanced x1 ![TIE Advanced x1]&#40;icons/galactic_empire/tie_advanced_x1_r@2x.png&#41;)

[//]: # (* ![TIE Avenger]&#40;icons/galactic_empire/tie_avenger@2x.png&#41; TIE Avenger ![TIE Avenger]&#40;icons/galactic_empire/tie_avenger_r@2x.png&#41;)

[//]: # (* ![TIE Bomber]&#40;icons/galactic_empire/tie_bomber@2x.png&#41; TIE Bomber ![TIE Bomber]&#40;icons/galactic_empire/tie_bomber_r@2x.png&#41;)

[//]: # (* ![TIE Crawler]&#40;icons/galactic_empire/tie_crawler@2x.png&#41; TIE Crawler ![TIE Crawler]&#40;icons/galactic_empire/tie_crawler_r@2x.png&#41;)

[//]: # (* ![TIE Fighter]&#40;icons/galactic_empire/tie_fighter@2x.png&#41; TIE Fighter ![TIE Fighter]&#40;icons/galactic_empire/tie_fighter_r@2x.png&#41;)

[//]: # (* ![TIE Interceptor]&#40;icons/galactic_empire/tie_interceptor@2x.png&#41; TIE Interceptor ![TIE Interceptor]&#40;icons/galactic_empire/tie_interceptor_r@2x.png&#41;)

[//]: # (* ![TIE Reaper]&#40;icons/galactic_empire/tie_reaper@2x.png&#41; TIE Reaper ![TIE Reaper]&#40;icons/galactic_empire/tie_reaper_r@2x.png&#41;)

[//]: # (* ![TIE Striker]&#40;icons/galactic_empire/tie_striker@2x.png&#41; TIE Striker ![TIE Striker]&#40;icons/galactic_empire/tie_striker_r@2x.png&#41;)

[//]: # (* ![Victory I-class Star Destroyer]&#40;icons/galactic_empire/victory_i_class_star_destroyer@2x.png&#41; Victory I-class Star Destroyer ![Victory I-class Star Destroyer]&#40;icons/galactic_empire/victory_i_class_star_destroyer_r@2x.png&#41;)

[//]: # ()
[//]: # (### Alliance to Restore the Republic)

[//]: # ()
[//]: # (* ![A-Wing]&#40;icons/rebel_alliance/a_wing@2x.png&#41; A-Wing ![A-Wing]&#40;icons/rebel_alliance/a_wing_r@2x.png&#41;)

[//]: # (* ![B-Wing]&#40;icons/rebel_alliance/b_wing@2x.png&#41; B-Wing ![B-Wing]&#40;icons/rebel_alliance/b_wing_r@2x.png&#41;)

[//]: # (* ![Ghost]&#40;icons/rebel_alliance/ghost@2x.png&#41; Ghost ![Ghost]&#40;icons/rebel_alliance/ghost_r@2x.png&#41;)

[//]: # (* ![GR-75 Medium Transport]&#40;icons/rebel_alliance/gr_75_transport@2x.png&#41; GR-75 Medium Transport ![GR-75 Medium Transport]&#40;icons/rebel_alliance/gr_75_transport_r@2x.png&#41;)

[//]: # (* ![Home One]&#40;icons/rebel_alliance/home_one@2x.png&#41; Home One ![Home One]&#40;icons/rebel_alliance/home_one_r@2x.png&#41;)

[//]: # (* ![MC80-Liberty Star Cruiser]&#40;icons/rebel_alliance/mc80_liberty_star_cruiser@2x.png&#41; MC80-Liberty Star Cruiser ![MC80-Liberty Star Cruiser]&#40;icons/rebel_alliance/mc80_liberty_star_cruiser_r@2x.png&#41;)

[//]: # (* ![Millennium Falcon]&#40;icons/rebel_alliance/millennium_falcon@2x.png&#41; Millennium Falcon ![Millennium Falcon]&#40;icons/rebel_alliance/millennium_falcon_r@2x.png&#41;)

[//]: # (* ![Nebulon-B Frigate]&#40;icons/rebel_alliance/nebulon_b_frigate@2x.png&#41; Nebulon-B Frigate ![Nebulon-B Frigate]&#40;icons/rebel_alliance/nebulon_b_frigate_r@2x.png&#41;)

[//]: # (* ![Phantom]&#40;icons/rebel_alliance/phantom@2x.png&#41; Phantom ![Phantom]&#40;icons/rebel_alliance/phantom_r@2x.png&#41;)

[//]: # (* ![Phantom II]&#40;icons/rebel_alliance/phantom_ii@2x.png&#41; Phantom II ![Phantom II]&#40;icons/rebel_alliance/phantom_ii_r@2x.png&#41;)

[//]: # (* ![Phoenix Nest]&#40;icons/rebel_alliance/phoenix_nest@2x.png&#41; Phoenix Nest ![Phoenix Nest]&#40;icons/rebel_alliance/phoenix_nest_r@2x.png&#41;)

[//]: # (* ![Profundity]&#40;icons/rebel_alliance/profundity@2x.png&#41; Profundity ![Profundity]&#40;icons/rebel_alliance/profundity_r@2x.png&#41;)

[//]: # (* ![Prototype B6]&#40;icons/rebel_alliance/prototype_b6@2x.png&#41; Prototype B6 ![Prototype B6]&#40;icons/rebel_alliance/prototype_b6_r@2x.png&#41;)

[//]: # (* ![Rogue Shadow]&#40;icons/rebel_alliance/rogue_shadow@2x.png&#41; Rogue Shadow ![Rogue Shadow]&#40;icons/rebel_alliance/rogue_shadow_r@2x.png&#41;)

[//]: # (* ![T-16 Skyhopper]&#40;icons/rebel_alliance/t_16_skyhopper@2x.png&#41; T-16 Skyhopper ![T-16 Skyhopper]&#40;icons/rebel_alliance/t_16_skyhopper_r@2x.png&#41;)

[//]: # (* ![T-47 Snowspeeder]&#40;icons/rebel_alliance/t_47_snowspeeder@2x.png&#41; T-47 Snowspeeder ![T-47 Snowspeeder]&#40;icons/rebel_alliance/t_47_snowspeeder_r@2x.png&#41;)

[//]: # (* ![T-65 X-wing starfighter]&#40;icons/rebel_alliance/t_65_x_wing_starfighter@2x.png&#41; T-65 X-Wing starfighter ![T-65 X-wing starfighter]&#40;icons/rebel_alliance/t_65_x_wing_starfighter_r@2x.png&#41;)

[//]: # (* ![Tantive IV]&#40;icons/rebel_alliance/tantive_iv@2x.png&#41; Tantive IV ![Tantive IV]&#40;icons/rebel_alliance/tantive_iv_r@2x.png&#41;)

[//]: # (* ![Taylander Shuttle]&#40;icons/rebel_alliance/taylander_shuttle@2x.png&#41; Taylander Shuttle ![Taylander Shuttle]&#40;icons/rebel_alliance/taylander_shuttle_r@2x.png&#41;)

[//]: # (* ![U-Wing]&#40;icons/rebel_alliance/u_wing@2x.png&#41; U-Wing ![U-Wing]&#40;icons/rebel_alliance/u_wing_r@2x.png&#41;)

[//]: # (* ![Y-Wing]&#40;icons/rebel_alliance/y_wing@2x.png&#41; Y-Wing ![Y-Wing]&#40;icons/rebel_alliance/y_wing_r@2x.png&#41;)

[//]: # ()
[//]: # (### First Order)

[//]: # ()
[//]: # (* ![First Order TIE Fighter]&#40;icons/first_order/first_order_tie_fighter@2x.png&#41; First Order TIE Fighter ![First Order TIE Fighter]&#40;icons/first_order/first_order_tie_fighter_r@2x.png&#41;)

[//]: # (* ![Starkiller Base]&#40;icons/first_order/starkiller_base@2x.png&#41; Starkiller Base ![Starkiller Base]&#40;icons/first_order/starkiller_base_r@2x.png&#41;)

[//]: # (* ![Supremacy]&#40;icons/first_order/supremacy@2x.png&#41; Supremacy ![Supremacy]&#40;icons/first_order/supremacy_r@2x.png&#41;)

[//]: # (* ![TIE Baron]&#40;icons/first_order/tie_baron@2x.png&#41; TIE Baron ![TIE Baron]&#40;icons/first_order/tie_baron_r@2x.png&#41;)

[//]: # (* ![TIE Dagger]&#40;icons/first_order/tie_dagger@2x.png&#41; TIE Dagger ![TIE Dagger]&#40;icons/first_order/tie_dagger_r@2x.png&#41;)

[//]: # (* ![TIE Silencer]&#40;icons/first_order/tie_silencer@2x.png&#41; TIE Silencer ![TIE Silencer]&#40;icons/first_order/tie_silencer_r@2x.png&#41;)

[//]: # ()
[//]: # (### New Republic)

[//]: # ()
[//]: # (* ![Starhawk-class battleship]&#40;icons/new_republic/starhawk_class_battleship@2x.png&#41; Starhawk-class battleship ![Starhawk-class battleship]&#40;icons/new_republic/starhawk_class_battleship_r@2x.png&#41;)

[//]: # ()
[//]: # (### Mandalorians)

[//]: # ()
[//]: # (* ![Din Djarin's N-1 Starfighter]&#40;icons/mandalorians/mandos_n_1_starfighter@2x.png&#41; Din Djarin's N-1 Starfighter ![Din Djarin's N-1 Starfighter]&#40;icons/mandalorians/mandos_n_1_starfighter_r@2x.png&#41;)

[//]: # ()
[//]: # (### Bounty Hunters)

[//]: # ()
[//]: # (* ![Boba Fett's Slave One]&#40;icons/bounty_hunters/boba_fetts_slave_one@2x.png&#41; Boba Fett's Slave One ![Boba Fett's Slave One]&#40;icons/bounty_hunters/boba_fetts_slave_one_r@2x.png&#41;)

[//]: # (* ![IG-2000]&#40;icons/bounty_hunters/ig_2000@2x.png&#41; IG-2000 ![IG-2000]&#40;icons/bounty_hunters/ig_2000_r@2x.png&#41;)

[//]: # (* ![Jango Fett's Slave One]&#40;icons/bounty_hunters/jango_fetts_slave_one@2x.png&#41; Jango Fett's Slave One ![Jango Fett's Slave One]&#40;icons/bounty_hunters/jango_fetts_slave_one_r@2x.png&#41;)

[//]: # (* ![Koro-2 all-environment Exodrive airspeeder]&#40;icons/bounty_hunters/koro_2_all_environment_exodrive_airspeeder@2x.png&#41; Koro-2 all-environment Exodrive airspeeder ![Koro-2 all-environment Exodrive airspeeder]&#40;icons/bounty_hunters/koro_2_all_environment_exodrive_airspeeder_r@2x.png&#41;)

[//]: # ()
[//]: # (### Scoundrels)

[//]: # ()
[//]: # (* ![Acushnet]&#40;icons/scoundrels/acushnet@2x.png&#41; Acushnet ![Acushnet]&#40;icons/scoundrels/acushnet_r@2x.png&#41;)

[//]: # (* ![Cloud City]&#40;icons/scoundrels/cloud_city@2x.png&#41; Cloud City ![Cloud City]&#40;icons/scoundrels/cloud_city_r@2x.png&#41;)

[//]: # (* ![Errant Venture]&#40;icons/scoundrels/errant_venture@2x.png&#41; Errant Venture ![Errant Venture]&#40;icons/scoundrels/errant_venture_r@2x.png&#41;)

[//]: # (* ![Khetanna]&#40;icons/scoundrels/khetanna@2x.png&#41; Khetanna ![Khetanna]&#40;icons/scoundrels/khetanna_r@2x.png&#41;)

[//]: # (* ![Last Chance]&#40;icons/scoundrels/last_chance@2x.png&#41; Last Chance ![Last Chance]&#40;icons/scoundrels/last_chance_r@2x.png&#41;)

[//]: # (* ![Sandcrawler]&#40;icons/scoundrels/sandcrawler@2x.png&#41; Sandcrawler ![Sandcrawler]&#40;icons/scoundrels/sandcrawler_r@2x.png&#41;)

[//]: # (* ![Storm IV Twin-Pod cloud car]&#40;icons/scoundrels/storm_iv_twin_pod_cloud_car@2x.png&#41; Storm IV Twin-Pod cloud car ![Storm IV Twin-Pod cloud car]&#40;icons/scoundrels/storm_iv_twin_pod_cloud_car_r@2x.png&#41;)

[//]: # (* ![Wild Karrde]&#40;icons/scoundrels/wild_karrde@2x.png&#41; Wild Karrde ![Wild Karrde]&#40;icons/scoundrels/wild_karrde_r@2x.png&#41;)

[//]: # ()
[//]: # (### Podracer)

[//]: # ()
[//]: # (* ![Aldar Beedo's Podracer]&#40;icons/podracer/aldar_beedos_podracer@2x.png&#41; Aldar Beedo's Podracer ![Aldar Beedo's Podracer]&#40;icons/podracer/aldar_beedos_podracer_r@2x.png&#41;)

[//]: # (* ![Anakin Skywalker's Podracer]&#40;icons/podracer/anakin_skywalkers_podracer@2x.png&#41; Anakin Skywalker's Podracer ![Anakin Skywalker's Podracer]&#40;icons/podracer/anakin_skywalkers_podracer_r@2x.png&#41;)

[//]: # (* ![Ark "Bumpy" Roose's Podracer]&#40;icons/podracer/ark_bumpy_rooses_podracer@2x.png&#41; Ark "Bumpy" Roose's Podracer ![Ark "Bumpy" Roose's Podracer]&#40;icons/podracer/ark_bumpy_rooses_podracer_r@2x.png&#41;)

[//]: # (* ![Ben Quadinaros' Podracer]&#40;icons/podracer/ben_quadinaros_podracer@2x.png&#41; Ben Quadinaros' Podracer ![Ben Quadinaros' Podracer]&#40;icons/podracer/ben_quadinaros_podracer_r@2x.png&#41;)

[//]: # (* ![Boles Roor's Podracer]&#40;icons/podracer/boles_roors_podracer@2x.png&#41; Boles Roor's Podracer ![Boles Roor's Podracer]&#40;icons/podracer/boles_roors_podracer_r@2x.png&#41;)

[//]: # (* ![Clegg Holdfast's Podracer]&#40;icons/podracer/clegg_holdfasts_podracer@2x.png&#41; Clegg Holdfast's Podracer ![Clegg Holdfast's Podracer]&#40;icons/podracer/clegg_holdfasts_podracer_r@2x.png&#41;)

[//]: # (* ![Dud Bolt's Podracer]&#40;icons/podracer/dud_bolts_podracer@2x.png&#41; Dud Bolt's Podracer ![Dud Bolt's Podracer]&#40;icons/podracer/dud_bolts_podracer_r@2x.png&#41;)

[//]: # (* ![Ebe Endocott's Podracer]&#40;icons/podracer/ebe_endocotts_podracer@2x.png&#41; Ebe Endocott's Podracer ![Ebe Endocott's Podracer]&#40;icons/podracer/ebe_endocotts_podracer_r@2x.png&#41;)

[//]: # (* ![Elan Mak's Podracer]&#40;icons/podracer/elan_maks_podracer@2x.png&#41; Elan Mak's Podracer ![Elan Mak's Podracer]&#40;icons/podracer/elan_maks_podracer_r@2x.png&#41;)

[//]: # (* ![Gasgano's Podracer]&#40;icons/podracer/gasganos_podracer@2x.png&#41; Gasgano's Podracer ![Gasgano's Podracer]&#40;icons/podracer/gasganos_podracer_r@2x.png&#41;)

[//]: # (* ![Mars Guo's Podracer]&#40;icons/podracer/mars_guos_podracer@2x.png&#41; Mars Guo's Podracer ![Mars Guo's Podracer]&#40;icons/podracer/mars_guos_podracer_r@2x.png&#41;)

[//]: # (* ![Mawhonic's Podracer]&#40;icons/podracer/mawhonics_podracer@2x.png&#41; Mawhonic's Podracer ![Mawhonic's Podracer]&#40;icons/podracer/mawhonics_podracer_r@2x.png&#41;)

[//]: # (* ![Neva Kee's Podracer]&#40;icons/podracer/neva_kees_podracer@2x.png&#41; Neva Kee's Podracer ![Neva Kee's Podracer]&#40;icons/podracer/neva_kees_podracer_r@2x.png&#41;)

[//]: # (* ![Ody Mandrell's Podracer]&#40;icons/podracer/ody_mandrells_podracer@2x.png&#41; Ody Mandrell's Podracer ![Ody Mandrell's Podracer]&#40;icons/podracer/ody_mandrells_podracer_r@2x.png&#41;)

[//]: # (* ![Ratts Tyerell's Podracer]&#40;icons/podracer/ratts_tyerells_podracer@2x.png&#41; Ratts Tyerell's Podracer ![Ratts Tyerell's Podracer]&#40;icons/podracer/ratts_tyerells_podracer_r@2x.png&#41;)

[//]: # (* ![Sebulba's Podracer]&#40;icons/podracer/sebulbas_podracer@2x.png&#41; Sebulba's Podracer ![Sebulba's Podracer]&#40;icons/podracer/sebulbas_podracer_r@2x.png&#41;)

[//]: # (* ![Teemto Pagalies' Podracer]&#40;icons/podracer/teemto_pagalies_podracer@2x.png&#41; Teemto Pagalies' Podracer ![Teemto Pagalies' Podracer]&#40;icons/podracer/teemto_pagalies_podracer_r@2x.png&#41;)

[//]: # (* ![Wan Sandage's Podracer]&#40;icons/podracer/wan_sandages_podracer@2x.png&#41; Wan Sandage's Podracer ![Wan Sandage's Podracer]&#40;icons/podracer/wan_sandages_podracer_r@2x.png&#41;)

<!-- Included vehicles end -->

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
