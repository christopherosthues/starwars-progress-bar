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

[//]: # (- [x] Review the [Legal Agreements]&#40;https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html&#41;.)

[//]: # (- [x] [Publish a plugin manually]&#40;https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate&#41; for the first time.)

[//]: # (- [x] Set the Plugin ID in the above README badges.)

[//]: # (- [x] Set the [Deployment Token]&#40;https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html&#41;.)

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
    <li>Display vehicle name in progressbar tooltip</li>
    <li>Display vehicle name in progressbar</li>
    <li>Display faction crest in progressbar</li>
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
## Included vehicles

### Trade Federation

* ![Armored Assault Tank Mk I](icons/trade_federation/armored_assault_tank@2x.png) Armored Assault Tank Mk I ![Armored Assault Tank Mk I](icons/trade_federation/armored_assault_tank_r@2x.png)
* ![Lucrehulk-class Droid control ship](icons/trade_federation/lucrehulk_class_droid_control_ship@2x.png) Lucrehulk-class Droid control ship ![Lucrehulk-class Droid control ship](icons/trade_federation/lucrehulk_class_droid_control_ship_r@2x.png)
* ![Multi-Troop Transport](icons/trade_federation/multi_troop_transport@2x.png) Multi-Troop Transport ![Multi-Troop Transport](icons/trade_federation/multi_troop_transport_r@2x.png)
* ![Platoon Attack Craft](icons/trade_federation/platoon_attack_craft@2x.png) Platoon Attack Craft ![Platoon Attack Craft](icons/trade_federation/platoon_attack_craft_r@2x.png)
* ![Sheathipede-class tranport shuttle](icons/trade_federation/sheathipede_class_shuttle@2x.png) Sheathipede-class tranport shuttle ![Sheathipede-class tranport shuttle](icons/trade_federation/sheathipede_class_shuttle_r@2x.png)
* ![STAP](icons/trade_federation/stap@2x.png) STAP ![STAP](icons/trade_federation/stap_r@2x.png)
* ![Trade Federation Landing Ship](icons/trade_federation/trade_federation_landing_ship@2x.png) Trade Federation Landing Ship ![Trade Federation Landing Ship](icons/trade_federation/trade_federation_landing_ship_r@2x.png)
* ![Vulture Droid](icons/trade_federation/vulture_droid@2x.png) Vulture Droid ![Vulture Droid](icons/trade_federation/vulture_droid_r@2x.png)

### Jedi Order

* ![Adi Gallia's Delta-7b-Aethersprite Starfighter](icons/jedi/adi_gallias_delta_7b_jedi_starfighter@2x.png) Adi Gallia's Delta-7b-Aethersprite Starfighter ![Adi Gallia's Delta-7b-Aethersprite Starfighter](icons/jedi/adi_gallias_delta_7b_jedi_starfighter_r@2x.png)
* ![Ahsoka Tano's Delta-7b-Aethersprite Starfighter](icons/jedi/ahsoka_tanos_delta_7b_jedi_starfighter@2x.png) Ahsoka Tano's Delta-7b-Aethersprite Starfighter ![Ahsoka Tano's Delta-7b-Aethersprite Starfighter](icons/jedi/ahsoka_tanos_delta_7b_jedi_starfighter_r@2x.png)
* ![Ahsoka Tano's Eta-2 Actis Interceptor](icons/jedi/ahsoka_tanos_eta_2_actis_interceptor@2x.png) Ahsoka Tano's Eta-2 Actis Interceptor ![Ahsoka Tano's Eta-2 Actis Interceptor](icons/jedi/ahsoka_tanos_eta_2_actis_interceptor_r@2x.png)
* ![Anakin Skywalker's Delta-7b-Aethersprite Starfighter](icons/jedi/anakin_skywalkers_delta_7b_jedi_starfighter@2x.png) Anakin Skywalker's Delta-7b-Aethersprite Starfighter ![Anakin Skywalker's Delta-7b-Aethersprite Starfighter](icons/jedi/anakin_skywalkers_delta_7b_jedi_starfighter_r@2x.png)
* ![Anakin Skywalker's Eta-2 Actis Interceptor](icons/jedi/anakin_skywalkers_eta_2_actis_interceptor@2x.png) Anakin Skywalker's Eta-2 Actis Interceptor ![Anakin Skywalker's Eta-2 Actis Interceptor](icons/jedi/anakin_skywalkers_eta_2_actis_interceptor_r@2x.png)
* ![Azure Angel](icons/jedi/azure_angel@2x.png) Azure Angel ![Azure Angel](icons/jedi/azure_angel_r@2x.png)
* ![Barriss Offee's Delta-7b-Aethersprite Starfighter](icons/jedi/barriss_offees_delta_7b_jedi_starfighter@2x.png) Barriss Offee's Delta-7b-Aethersprite Starfighter ![Barriss Offee's Delta-7b-Aethersprite Starfighter](icons/jedi/barriss_offees_delta_7b_jedi_starfighter_r@2x.png)
* ![Blade of Dorin](icons/jedi/blade_of_dorin@2x.png) Blade of Dorin ![Blade of Dorin](icons/jedi/blade_of_dorin_r@2x.png)
* ![DC0052 Speeder](icons/jedi/dc0052_speeder@2x.png) DC0052 Speeder ![DC0052 Speeder](icons/jedi/dc0052_speeder_r@2x.png)
* ![Eta-class Shuttle](icons/jedi/eta_class_shuttle@2x.png) Eta-class Shuttle ![Eta-class Shuttle](icons/jedi/eta_class_shuttle_r@2x.png)
* ![Kit Fisto's Delta-7b-Aethersprite Starfighter](icons/jedi/kit_fistos_delta_7b_jedi_starfighter@2x.png) Kit Fisto's Delta-7b-Aethersprite Starfighter ![Kit Fisto's Delta-7b-Aethersprite Starfighter](icons/jedi/kit_fistos_delta_7b_jedi_starfighter_r@2x.png)
* ![Luminara Unduli's Delta-7b-Aethersprite Starfighter](icons/jedi/luminara_undulis_delta_7b_jedi_starfighter@2x.png) Luminara Unduli's Delta-7b-Aethersprite Starfighter ![Luminara Unduli's Delta-7b-Aethersprite Starfighter](icons/jedi/luminara_undulis_delta_7b_jedi_starfighter_r@2x.png)
* ![Mace Windu's Delta-7b-Aethersprite Starfighter](icons/jedi/mace_windus_delta_7b_jedi_starfighter@2x.png) Mace Windu's Delta-7b-Aethersprite Starfighter ![Mace Windu's Delta-7b-Aethersprite Starfighter](icons/jedi/mace_windus_delta_7b_jedi_starfighter_r@2x.png)
* ![Obi-Wan Kenobi's Delta-7-Aethersprite Starfighter](icons/jedi/obi_wan_kenobis_delta_7_jedi_starfighter@2x.png) Obi-Wan Kenobi's Delta-7-Aethersprite Starfighter ![Obi-Wan Kenobi's Delta-7-Aethersprite Starfighter](icons/jedi/obi_wan_kenobis_delta_7_jedi_starfighter_r@2x.png)
* ![Obi-Wan Kenobi's Eta-2 Actis Interceptor](icons/jedi/obi_wan_kenobis_eta_2_actis_interceptor@2x.png) Obi-Wan Kenobi's Eta-2 Actis Interceptor ![Obi-Wan Kenobi's Eta-2 Actis Interceptor](icons/jedi/obi_wan_kenobis_eta_2_actis_interceptor_r@2x.png)
* ![Praxis Mk I turbo speeder](icons/jedi/praxis_mk_i_turbo_speeder@2x.png) Praxis Mk I turbo speeder ![Praxis Mk I turbo speeder](icons/jedi/praxis_mk_i_turbo_speeder_r@2x.png)
* ![Saesee Tiin's Delta-7b-Aethersprite Starfighter](icons/jedi/saesee_tiins_delta_7b_jedi_starfighter@2x.png) Saesee Tiin's Delta-7b-Aethersprite Starfighter ![Saesee Tiin's Delta-7b-Aethersprite Starfighter](icons/jedi/saesee_tiins_delta_7b_jedi_starfighter_r@2x.png)

### Galactic Republic

* ![AA-9 Coruscant Freighter](icons/galactic_republic/aa_9_coruscant_freighter@2x.png) AA-9 Coruscant Freighter ![AA-9 Coruscant Freighter](icons/galactic_republic/aa_9_coruscant_freighter_r@2x.png)
* ![Acclamator-class Assault Ship](icons/galactic_republic/acclamator_class_assault_ship@2x.png) Acclamator-class Assault Ship ![Acclamator-class Assault Ship](icons/galactic_republic/acclamator_class_assault_ship_r@2x.png)
* ![Acclamator-II-class Assault Ship](icons/galactic_republic/acclamator_ii_class_assault_ship@2x.png) Acclamator-II-class Assault Ship ![Acclamator-II-class Assault Ship](icons/galactic_republic/acclamator_ii_class_assault_ship_r@2x.png)
* ![ARC-170 Starfighter](icons/galactic_republic/arc_170_starfighter@2x.png) ARC-170 Starfighter ![ARC-170 Starfighter](icons/galactic_republic/arc_170_starfighter_r@2x.png)
* ![AT-AP](icons/galactic_republic/at_ap@2x.png) AT-AP ![AT-AP](icons/galactic_republic/at_ap_r@2x.png)
* ![Berenko-class gondola speeder](icons/galactic_republic/berenko_class_gondola_speeder@2x.png) Berenko-class gondola speeder ![Berenko-class gondola speeder](icons/galactic_republic/berenko_class_gondola_speeder_r@2x.png)
* ![BTL-B Y-Wing](icons/galactic_republic/btl_b_y_wing@2x.png) BTL-B Y-Wing ![BTL-B Y-Wing](icons/galactic_republic/btl_b_y_wing_r@2x.png)
* ![Captain Rex's BTL-B Y-Wing](icons/galactic_republic/captain_rex_y_wing@2x.png) Captain Rex's BTL-B Y-Wing ![Captain Rex's BTL-B Y-Wing](icons/galactic_republic/captain_rex_y_wing_r@2x.png)
* ![CSS-1 Corellian Star Shuttle](icons/galactic_republic/css_1_corellian_star_shuttle@2x.png) CSS-1 Corellian Star Shuttle ![CSS-1 Corellian Star Shuttle](icons/galactic_republic/css_1_corellian_star_shuttle_r@2x.png)
* ![H-Type Naboo Yacht](icons/galactic_republic/h_type_naboo_yacht@2x.png) H-Type Naboo Yacht ![H-Type Naboo Yacht](icons/galactic_republic/h_type_naboo_yacht_r@2x.png)
* ![Heavy Assault Vehicle/Wheeled A6 Juggernaut](icons/galactic_republic/havw_a6_juggernaut@2x.png) Heavy Assault Vehicle/Wheeled A6 Juggernaut ![Heavy Assault Vehicle/Wheeled A6 Juggernaut](icons/galactic_republic/havw_a6_juggernaut_r@2x.png)
* ![Infantry Support Platform](icons/galactic_republic/infantry_support_platform@2x.png) Infantry Support Platform ![Infantry Support Platform](icons/galactic_republic/infantry_support_platform_r@2x.png)
* ![J-Type Diplomatic Barge](icons/galactic_republic/j_type_diplomatic_barge@2x.png) J-Type Diplomatic Barge ![J-Type Diplomatic Barge](icons/galactic_republic/j_type_diplomatic_barge_r@2x.png)
* ![J-Type Naboo Star Skiff](icons/galactic_republic/j_type_naboo_star_skiff@2x.png) J-Type Naboo Star Skiff ![J-Type Naboo Star Skiff](icons/galactic_republic/j_type_naboo_star_skiff_r@2x.png)
* ![LAAT/i](icons/galactic_republic/laat_i@2x.png) LAAT/i ![LAAT/i](icons/galactic_republic/laat_i_r@2x.png)
* ![N-1 Starfighter](icons/galactic_republic/n_1_starfighter@2x.png) N-1 Starfighter ![N-1 Starfighter](icons/galactic_republic/n_1_starfighter_r@2x.png)
* ![Naboo Royal Starship](icons/galactic_republic/naboo_royal_starship@2x.png) Naboo Royal Starship ![Naboo Royal Starship](icons/galactic_republic/naboo_royal_starship_r@2x.png)
* ![Seraph-class Flash speeder](icons/galactic_republic/seraph_class_speeder@2x.png) Seraph-class Flash speeder ![Seraph-class Flash speeder](icons/galactic_republic/seraph_class_speeder_r@2x.png)
* ![Syluire-31 hyperdrive docking ring](icons/galactic_republic/syluire_31_hyperspace_docking_ring@2x.png) Syluire-31 hyperdrive docking ring ![Syluire-31 hyperdrive docking ring](icons/galactic_republic/syluire_31_hyperspace_docking_ring_r@2x.png)
* ![V-19 Torrent Starfighter](icons/galactic_republic/v_19_torrent_starfighter@2x.png) V-19 Torrent Starfighter ![V-19 Torrent Starfighter](icons/galactic_republic/v_19_torrent_starfighter_r@2x.png)
* ![V-Wing](icons/galactic_republic/v_wing@2x.png) V-Wing ![V-Wing](icons/galactic_republic/v_wing_r@2x.png)
* ![Venator-class Star Destroyer](icons/galactic_republic/venator_class_star_destroyer@2x.png) Venator-class Star Destroyer ![Venator-class Star Destroyer](icons/galactic_republic/venator_class_star_destroyer_r@2x.png)
* ![XJ-6 Airspeeder](icons/galactic_republic/xj_6_airspeeder@2x.png) XJ-6 Airspeeder ![XJ-6 Airspeeder](icons/galactic_republic/xj_6_airspeeder_r@2x.png)
* ![Z-95 Headhunter](icons/galactic_republic/z_95_headhunter@2x.png) Z-95 Headhunter ![Z-95 Headhunter](icons/galactic_republic/z_95_headhunter_r@2x.png)

### Confederacy of independent systems

* ![Armored Assault Tank Mk I](icons/confederacy_of_independent_systems/armored_assault_tank_cis@2x.png) Armored Assault Tank Mk I ![Armored Assault Tank Mk I](icons/confederacy_of_independent_systems/armored_assault_tank_cis_r@2x.png)
* ![Bloodfin](icons/confederacy_of_independent_systems/bloodfin@2x.png) Bloodfin ![Bloodfin](icons/confederacy_of_independent_systems/bloodfin_r@2x.png)
* ![Core ship](icons/confederacy_of_independent_systems/core_ship@2x.png) Core ship ![Core ship](icons/confederacy_of_independent_systems/core_ship_r@2x.png)
* ![DH-Omni Support Vessel](icons/confederacy_of_independent_systems/separatist_supply_ship@2x.png) DH-Omni Support Vessel ![DH-Omni Support Vessel](icons/confederacy_of_independent_systems/separatist_supply_ship_r@2x.png)
* ![Diamond-class Cruiser](icons/confederacy_of_independent_systems/diamond_class_cruiser@2x.png) Diamond-class Cruiser ![Diamond-class Cruiser](icons/confederacy_of_independent_systems/diamond_class_cruiser_r@2x.png)
* ![Droid Tri-Fighter](icons/confederacy_of_independent_systems/droid_tri_fighter@2x.png) Droid Tri-Fighter ![Droid Tri-Fighter](icons/confederacy_of_independent_systems/droid_tri_fighter_r@2x.png)
* ![Fanblade](icons/confederacy_of_independent_systems/fanblade@2x.png) Fanblade ![Fanblade](icons/confederacy_of_independent_systems/fanblade_r@2x.png)
* ![Heavy Missile Platform droid gunship](icons/confederacy_of_independent_systems/heavy_missile_platform_droid_gunship@2x.png) Heavy Missile Platform droid gunship ![Heavy Missile Platform droid gunship](icons/confederacy_of_independent_systems/heavy_missile_platform_droid_gunship_r@2x.png)
* ![IG-227 Hailfire class Droid tank](icons/confederacy_of_independent_systems/ig_227_hailfire_class_droid_tank@2x.png) IG-227 Hailfire class Droid tank ![IG-227 Hailfire class Droid tank](icons/confederacy_of_independent_systems/ig_227_hailfire_class_droid_tank_r@2x.png)
* ![Invisible Hand](icons/confederacy_of_independent_systems/invisible_hand@2x.png) Invisible Hand ![Invisible Hand](icons/confederacy_of_independent_systems/invisible_hand_r@2x.png)
* ![Landing Craft](icons/confederacy_of_independent_systems/landing_craft@2x.png) Landing Craft ![Landing Craft](icons/confederacy_of_independent_systems/landing_craft_r@2x.png)
* ![Lucrehulk-class Battleship](icons/confederacy_of_independent_systems/lucrehulk_class_battleship@2x.png) Lucrehulk-class Battleship ![Lucrehulk-class Battleship](icons/confederacy_of_independent_systems/lucrehulk_class_battleship_r@2x.png)
* ![Malevolence](icons/confederacy_of_independent_systems/malevolence@2x.png) Malevolence ![Malevolence](icons/confederacy_of_independent_systems/malevolence_r@2x.png)
* ![Mankvim-814 Interceptor](icons/confederacy_of_independent_systems/mankvim_814_interceptor@2x.png) Mankvim-814 Interceptor ![Mankvim-814 Interceptor](icons/confederacy_of_independent_systems/mankvim_814_interceptor_r@2x.png)
* ![Maxillipede-class Shuttle](icons/confederacy_of_independent_systems/maxillipede_shuttle@2x.png) Maxillipede-class Shuttle ![Maxillipede-class Shuttle](icons/confederacy_of_independent_systems/maxillipede_shuttle_r@2x.png)
* ![Multi-Troop Transport](icons/confederacy_of_independent_systems/multi_troop_transport_cis@2x.png) Multi-Troop Transport ![Multi-Troop Transport](icons/confederacy_of_independent_systems/multi_troop_transport_cis_r@2x.png)
* ![Munificent-class Star Frigate](icons/confederacy_of_independent_systems/munificent_class_star_frigate@2x.png) Munificent-class Star Frigate ![Munificent-class Star Frigate](icons/confederacy_of_independent_systems/munificent_class_star_frigate_r@2x.png)
* ![Nantex-class Starfighter](icons/confederacy_of_independent_systems/nantex_class_starfighter@2x.png) Nantex-class Starfighter ![Nantex-class Starfighter](icons/confederacy_of_independent_systems/nantex_class_starfighter_r@2x.png)
* ![NR-N99 Persuader-class droid enforcer](icons/confederacy_of_independent_systems/nr_n99_persuader_class_droid_enforcer@2x.png) NR-N99 Persuader-class droid enforcer ![NR-N99 Persuader-class droid enforcer](icons/confederacy_of_independent_systems/nr_n99_persuader_class_droid_enforcer_r@2x.png)
* ![OG-9 homing spider droid](icons/confederacy_of_independent_systems/og_9_homing_spider_droid@2x.png) OG-9 homing spider droid ![OG-9 homing spider droid](icons/confederacy_of_independent_systems/og_9_homing_spider_droid_r@2x.png)
* ![Platoon Attack Craft](icons/confederacy_of_independent_systems/platoon_attack_craft_cis@2x.png) Platoon Attack Craft ![Platoon Attack Craft](icons/confederacy_of_independent_systems/platoon_attack_craft_cis_r@2x.png)
* ![Porax-38 Starfighter](icons/confederacy_of_independent_systems/porax_38_starfighter@2x.png) Porax-38 Starfighter ![Porax-38 Starfighter](icons/confederacy_of_independent_systems/porax_38_starfighter_r@2x.png)
* ![Punworcca 116-class interstellar sloop](icons/confederacy_of_independent_systems/punworcca_116_class_interstellar_sloop@2x.png) Punworcca 116-class interstellar sloop ![Punworcca 116-class interstellar sloop](icons/confederacy_of_independent_systems/punworcca_116_class_interstellar_sloop_r@2x.png)
* ![Recusant-class Destroyer](icons/confederacy_of_independent_systems/recusant_class_destroyer@2x.png) Recusant-class Destroyer ![Recusant-class Destroyer](icons/confederacy_of_independent_systems/recusant_class_destroyer_r@2x.png)
* ![Scimitar](icons/confederacy_of_independent_systems/scimitar@2x.png) Scimitar ![Scimitar](icons/confederacy_of_independent_systems/scimitar_r@2x.png)
* ![Sheathipede-class tranport shuttle](icons/confederacy_of_independent_systems/sheathipede_class_shuttle_cis@2x.png) Sheathipede-class tranport shuttle ![Sheathipede-class tranport shuttle](icons/confederacy_of_independent_systems/sheathipede_class_shuttle_cis_r@2x.png)
* ![Sheathipede-class Type B shuttle](icons/confederacy_of_independent_systems/sheathipede_class_type_b_shuttle@2x.png) Sheathipede-class Type B shuttle ![Sheathipede-class Type B shuttle](icons/confederacy_of_independent_systems/sheathipede_class_type_b_shuttle_r@2x.png)
* ![Soulless One](icons/confederacy_of_independent_systems/soulless_one@2x.png) Soulless One ![Soulless One](icons/confederacy_of_independent_systems/soulless_one_r@2x.png)
* ![STAP](icons/confederacy_of_independent_systems/stap_cis@2x.png) STAP ![STAP](icons/confederacy_of_independent_systems/stap_cis_r@2x.png)
* ![Vulture Droid](icons/confederacy_of_independent_systems/vulture_droid_cis@2x.png) Vulture Droid ![Vulture Droid](icons/confederacy_of_independent_systems/vulture_droid_cis_r@2x.png)

### Galactic Empire

* ![AT-ACT](icons/galactic_empire/at_act@2x.png) AT-ACT ![AT-ACT](icons/galactic_empire/at_act_r@2x.png)
* ![AT-AT](icons/galactic_empire/at_at@2x.png) AT-AT ![AT-AT](icons/galactic_empire/at_at_r@2x.png)
* ![AT-ST](icons/galactic_empire/at_st@2x.png) AT-ST ![AT-ST](icons/galactic_empire/at_st_r@2x.png)
* ![Eclipse](icons/galactic_empire/eclipse@2x.png) Eclipse ![Eclipse](icons/galactic_empire/eclipse_r@2x.png)
* ![Executor](icons/galactic_empire/executor@2x.png) Executor ![Executor](icons/galactic_empire/executor_r@2x.png)
* ![First Death Star](icons/galactic_empire/first_death_star@2x.png) First Death Star ![First Death Star](icons/galactic_empire/first_death_star_r@2x.png)
* ![Imperial II-class Star Destroyer](icons/galactic_empire/imperial_class_star_destroyer@2x.png) Imperial II-class Star Destroyer ![Imperial II-class Star Destroyer](icons/galactic_empire/imperial_class_star_destroyer_r@2x.png)
* ![Interdictor-class Star Destroyer](icons/galactic_empire/interdictor_class_star_destroyer@2x.png) Interdictor-class Star Destroyer ![Interdictor-class Star Destroyer](icons/galactic_empire/interdictor_class_star_destroyer_r@2x.png)
* ![Lambda-class Shuttle](icons/galactic_empire/lambda_class_shuttle@2x.png) Lambda-class Shuttle ![Lambda-class Shuttle](icons/galactic_empire/lambda_class_shuttle_r@2x.png)
* ![Mining Guild TIE Fighter](icons/galactic_empire/mining_guild_tie_fighter@2x.png) Mining Guild TIE Fighter ![Mining Guild TIE Fighter](icons/galactic_empire/mining_guild_tie_fighter_r@2x.png)
* ![Onager-class Star Destroyer](icons/galactic_empire/onager_class_star_destroyer@2x.png) Onager-class Star Destroyer ![Onager-class Star Destroyer](icons/galactic_empire/onager_class_star_destroyer_r@2x.png)
* ![Second Death Star](icons/galactic_empire/second_death_star@2x.png) Second Death Star ![Second Death Star](icons/galactic_empire/second_death_star_r@2x.png)
* ![Sentinel-class Shuttle](icons/galactic_empire/sentinel_class_shuttle@2x.png) Sentinel-class Shuttle ![Sentinel-class Shuttle](icons/galactic_empire/sentinel_class_shuttle_r@2x.png)
* ![Tartan-class patrol cruiser](icons/galactic_empire/tartan_class_patrol_cruiser@2x.png) Tartan-class patrol cruiser ![Tartan-class patrol cruiser](icons/galactic_empire/tartan_class_patrol_cruiser_r@2x.png)
* ![Theta-class Shuttle](icons/galactic_empire/theta_class_shuttle@2x.png) Theta-class Shuttle ![Theta-class Shuttle](icons/galactic_empire/theta_class_shuttle_r@2x.png)
* ![TIE Advanced x1](icons/galactic_empire/tie_advanced_x1@2x.png) TIE Advanced x1 ![TIE Advanced x1](icons/galactic_empire/tie_advanced_x1_r@2x.png)
* ![TIE Avenger](icons/galactic_empire/tie_avenger@2x.png) TIE Avenger ![TIE Avenger](icons/galactic_empire/tie_avenger_r@2x.png)
* ![TIE Bomber](icons/galactic_empire/tie_bomber@2x.png) TIE Bomber ![TIE Bomber](icons/galactic_empire/tie_bomber_r@2x.png)
* ![TIE Crawler](icons/galactic_empire/tie_crawler@2x.png) TIE Crawler ![TIE Crawler](icons/galactic_empire/tie_crawler_r@2x.png)
* ![TIE Fighter](icons/galactic_empire/tie_fighter@2x.png) TIE Fighter ![TIE Fighter](icons/galactic_empire/tie_fighter_r@2x.png)
* ![TIE Interceptor](icons/galactic_empire/tie_interceptor@2x.png) TIE Interceptor ![TIE Interceptor](icons/galactic_empire/tie_interceptor_r@2x.png)
* ![TIE Reaper](icons/galactic_empire/tie_reaper@2x.png) TIE Reaper ![TIE Reaper](icons/galactic_empire/tie_reaper_r@2x.png)
* ![TIE Striker](icons/galactic_empire/tie_striker@2x.png) TIE Striker ![TIE Striker](icons/galactic_empire/tie_striker_r@2x.png)
* ![Victory I-class Star Destroyer](icons/galactic_empire/victory_i_class_star_destroyer@2x.png) Victory I-class Star Destroyer ![Victory I-class Star Destroyer](icons/galactic_empire/victory_i_class_star_destroyer_r@2x.png)

### Alliance to Restore the Republic

* ![A-Wing](icons/rebel_alliance/a_wing@2x.png) A-Wing ![A-Wing](icons/rebel_alliance/a_wing_r@2x.png)
* ![B-Wing](icons/rebel_alliance/b_wing@2x.png) B-Wing ![B-Wing](icons/rebel_alliance/b_wing_r@2x.png)
* ![Ghost](icons/rebel_alliance/ghost@2x.png) Ghost ![Ghost](icons/rebel_alliance/ghost_r@2x.png)
* ![GR-75 Medium Transport](icons/rebel_alliance/gr_75_transport@2x.png) GR-75 Medium Transport ![GR-75 Medium Transport](icons/rebel_alliance/gr_75_transport_r@2x.png)
* ![Home One](icons/rebel_alliance/home_one@2x.png) Home One ![Home One](icons/rebel_alliance/home_one_r@2x.png)
* ![MC80-Liberty Star Cruiser](icons/rebel_alliance/mc80_liberty_star_cruiser@2x.png) MC80-Liberty Star Cruiser ![MC80-Liberty Star Cruiser](icons/rebel_alliance/mc80_liberty_star_cruiser_r@2x.png)
* ![Millennium Falcon](icons/rebel_alliance/millennium_falcon@2x.png) Millennium Falcon ![Millennium Falcon](icons/rebel_alliance/millennium_falcon_r@2x.png)
* ![Nebulon-B Frigate](icons/rebel_alliance/nebulon_b_frigate@2x.png) Nebulon-B Frigate ![Nebulon-B Frigate](icons/rebel_alliance/nebulon_b_frigate_r@2x.png)
* ![Phantom](icons/rebel_alliance/phantom@2x.png) Phantom ![Phantom](icons/rebel_alliance/phantom_r@2x.png)
* ![Phantom II](icons/rebel_alliance/phantom_ii@2x.png) Phantom II ![Phantom II](icons/rebel_alliance/phantom_ii_r@2x.png)
* ![Prototype B6](icons/rebel_alliance/prototype_b6@2x.png) Prototype B6 ![Prototype B6](icons/rebel_alliance/prototype_b6_r@2x.png)
* ![Rogue Shadow](icons/rebel_alliance/rogue_shadow@2x.png) Rogue Shadow ![Rogue Shadow](icons/rebel_alliance/rogue_shadow_r@2x.png)
* ![T-16 Skyhopper](icons/rebel_alliance/t_16_skyhopper@2x.png) T-16 Skyhopper ![T-16 Skyhopper](icons/rebel_alliance/t_16_skyhopper_r@2x.png)
* ![T-47 Snowspeeder](icons/rebel_alliance/t_47_snowspeeder@2x.png) T-47 Snowspeeder ![T-47 Snowspeeder](icons/rebel_alliance/t_47_snowspeeder_r@2x.png)
* ![Tantive IV](icons/rebel_alliance/tantive_iv@2x.png) Tantive IV ![Tantive IV](icons/rebel_alliance/tantive_iv_r@2x.png)
* ![Taylander Shuttle](icons/rebel_alliance/taylander_shuttle@2x.png) Taylander Shuttle ![Taylander Shuttle](icons/rebel_alliance/taylander_shuttle_r@2x.png)
* ![U-Wing](icons/rebel_alliance/u_wing@2x.png) U-Wing ![U-Wing](icons/rebel_alliance/u_wing_r@2x.png)
* ![X-Wing](icons/rebel_alliance/x_wing@2x.png) X-Wing ![X-Wing](icons/rebel_alliance/x_wing_r@2x.png)
* ![Y-Wing](icons/rebel_alliance/y_wing@2x.png) Y-Wing ![Y-Wing](icons/rebel_alliance/y_wing_r@2x.png)

### First Order

* ![First Order TIE Fighter](icons/first_order/first_order_tie_fighter@2x.png) First Order TIE Fighter ![First Order TIE Fighter](icons/first_order/first_order_tie_fighter_r@2x.png)
* ![TIE Baron](icons/first_order/tie_baron@2x.png) TIE Baron ![TIE Baron](icons/first_order/tie_baron_r@2x.png)
* ![TIE Dagger](icons/first_order/tie_dagger@2x.png) TIE Dagger ![TIE Dagger](icons/first_order/tie_dagger_r@2x.png)

### Bounty Hunters

* ![Boba Fett's Slave One](icons/bounty_hunters/boba_fetts_slave_one@2x.png) Boba Fett's Slave One ![Boba Fett's Slave One](icons/bounty_hunters/boba_fetts_slave_one_r@2x.png)
* ![IG-2000](icons/bounty_hunters/ig_2000@2x.png) IG-2000 ![IG-2000](icons/bounty_hunters/ig_2000_r@2x.png)
* ![Jango Fett's Slave One](icons/bounty_hunters/jango_fetts_slave_one@2x.png) Jango Fett's Slave One ![Jango Fett's Slave One](icons/bounty_hunters/jango_fetts_slave_one_r@2x.png)
* ![Koro-2 all-environment Exodrive airspeeder](icons/bounty_hunters/koro_2_all_environment_exodrive_airspeeder@2x.png) Koro-2 all-environment Exodrive airspeeder ![Koro-2 all-environment Exodrive airspeeder](icons/bounty_hunters/koro_2_all_environment_exodrive_airspeeder_r@2x.png)

### Scoundrels

* ![Acushnet](icons/scoundrels/acushnet@2x.png) Acushnet ![Acushnet](icons/scoundrels/acushnet_r@2x.png)
* ![Cloud City](icons/scoundrels/cloud_city@2x.png) Cloud City ![Cloud City](icons/scoundrels/cloud_city_r@2x.png)
* ![Errant Venture](icons/scoundrels/errant_venture@2x.png) Errant Venture ![Errant Venture](icons/scoundrels/errant_venture_r@2x.png)
* ![Khetanna](icons/scoundrels/khetanna@2x.png) Khetanna ![Khetanna](icons/scoundrels/khetanna_r@2x.png)
* ![Last Chance](icons/scoundrels/last_chance@2x.png) Last Chance ![Last Chance](icons/scoundrels/last_chance_r@2x.png)
* ![Sandcrawler](icons/scoundrels/sandcrawler@2x.png) Sandcrawler ![Sandcrawler](icons/scoundrels/sandcrawler_r@2x.png)
* ![Storm IV Twin-Pod cloud car](icons/scoundrels/storm_iv_twin_pod_cloud_car@2x.png) Storm IV Twin-Pod cloud car ![Storm IV Twin-Pod cloud car](icons/scoundrels/storm_iv_twin_pod_cloud_car_r@2x.png)
* ![Wild Karrde](icons/scoundrels/wild_karrde@2x.png) Wild Karrde ![Wild Karrde](icons/scoundrels/wild_karrde_r@2x.png)

### Podracer

* ![Aldar Beedo's Podracer](icons/podracer/aldar_beedos_podracer@2x.png) Aldar Beedo's Podracer ![Aldar Beedo's Podracer](icons/podracer/aldar_beedos_podracer_r@2x.png)
* ![Anakin Skywalker's Podracer](icons/podracer/anakin_skywalkers_podracer@2x.png) Anakin Skywalker's Podracer ![Anakin Skywalker's Podracer](icons/podracer/anakin_skywalkers_podracer_r@2x.png)
* ![Ark "Bumpy" Roose's Podracer](icons/podracer/ark_bumpy_rooses_podracer@2x.png) Ark "Bumpy" Roose's Podracer ![Ark "Bumpy" Roose's Podracer](icons/podracer/ark_bumpy_rooses_podracer_r@2x.png)
* ![Ben Quadinaros' Podracer](icons/podracer/ben_quadinaros_podracer@2x.png) Ben Quadinaros' Podracer ![Ben Quadinaros' Podracer](icons/podracer/ben_quadinaros_podracer_r@2x.png)
* ![Boles Roor's Podracer](icons/podracer/boles_roors_podracer@2x.png) Boles Roor's Podracer ![Boles Roor's Podracer](icons/podracer/boles_roors_podracer_r@2x.png)
* ![Clegg Holdfast's Podracer](icons/podracer/clegg_holdfasts_podracer@2x.png) Clegg Holdfast's Podracer ![Clegg Holdfast's Podracer](icons/podracer/clegg_holdfasts_podracer_r@2x.png)
* ![Dud Bolt's Podracer](icons/podracer/dud_bolts_podracer@2x.png) Dud Bolt's Podracer ![Dud Bolt's Podracer](icons/podracer/dud_bolts_podracer_r@2x.png)
* ![Ebe Endocott's Podracer](icons/podracer/ebe_endocotts_podracer@2x.png) Ebe Endocott's Podracer ![Ebe Endocott's Podracer](icons/podracer/ebe_endocotts_podracer_r@2x.png)
* ![Elan Mak's Podracer](icons/podracer/elan_maks_podracer@2x.png) Elan Mak's Podracer ![Elan Mak's Podracer](icons/podracer/elan_maks_podracer_r@2x.png)
* ![Gasgano's Podracer](icons/podracer/gasganos_podracer@2x.png) Gasgano's Podracer ![Gasgano's Podracer](icons/podracer/gasganos_podracer_r@2x.png)
* ![Mars Guo's Podracer](icons/podracer/mars_guos_podracer@2x.png) Mars Guo's Podracer ![Mars Guo's Podracer](icons/podracer/mars_guos_podracer_r@2x.png)
* ![Mawhonic's Podracer](icons/podracer/mawhonics_podracer@2x.png) Mawhonic's Podracer ![Mawhonic's Podracer](icons/podracer/mawhonics_podracer_r@2x.png)
* ![Neva Kee's Podracer](icons/podracer/neva_kees_podracer@2x.png) Neva Kee's Podracer ![Neva Kee's Podracer](icons/podracer/neva_kees_podracer_r@2x.png)
* ![Ody Mandrell's Podracer](icons/podracer/ody_mandrells_podracer@2x.png) Ody Mandrell's Podracer ![Ody Mandrell's Podracer](icons/podracer/ody_mandrells_podracer_r@2x.png)
* ![Ratts Tyerell's Podracer](icons/podracer/ratts_tyerells_podracer@2x.png) Ratts Tyerell's Podracer ![Ratts Tyerell's Podracer](icons/podracer/ratts_tyerells_podracer_r@2x.png)
* ![Sebulba's Podracer](icons/podracer/sebulbas_podracer@2x.png) Sebulba's Podracer ![Sebulba's Podracer](icons/podracer/sebulbas_podracer_r@2x.png)
* ![Teemto Pagalies' Podracer](icons/podracer/teemto_pagalies_podracer@2x.png) Teemto Pagalies' Podracer ![Teemto Pagalies' Podracer](icons/podracer/teemto_pagalies_podracer_r@2x.png)
* ![Wan Sandage's Podracer](icons/podracer/wan_sandages_podracer@2x.png) Wan Sandage's Podracer ![Wan Sandage's Podracer](icons/podracer/wan_sandages_podracer_r@2x.png)

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
