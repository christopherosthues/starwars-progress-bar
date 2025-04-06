package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants

enum class SelectionType(val localizationKey: String) {
    INORDER_NAME("inorder_name"),
    REVERSE_ORDER_NAME("reverse_order_name"),
    INORDER_FACTION("inorder_faction"),
    REVERSE_ORDER_FACTION("reverse_order_faction"),
    RANDOM_ALL("random_all"),
    RANDOM_NOT_DISPLAYED("random_not_displayed"), ;

    override fun toString(): String = StarWarsBundle.message("${BundleConstants.SELECTOR}.$localizationKey")
}
