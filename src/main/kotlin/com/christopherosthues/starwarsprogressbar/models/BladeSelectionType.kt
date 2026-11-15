package com.christopherosthues.starwarsprogressbar.models

/**
 * Enum for variant selection modes used by rendering code to determine layout strategy.
 */
internal enum class BladeSelectionType {
    /** Full weapon display — all blades render simultaneously (current behavior) */
    FULL_WEAPON,

    /** Single blade variant — individual blade with adjusted hilt positioning and space allocation */
    SINGLE_BLADE,
}
