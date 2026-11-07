package com.christopherosthues.starwarsprogressbar.helpers

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import org.junit.jupiter.api.Assertions

internal fun assertEntityOrder3Items(
    index0: Int,
    index1: Int,
    index2: Int,
    entities: List<StarWarsEntity>,
    result: List<StarWarsEntity>,
) {
    Assertions.assertAll(
        { Assertions.assertEquals(3, result.size) },
        { Assertions.assertEquals(entities[index0], result[0]) },
        { Assertions.assertEquals(entities[index1], result[1]) },
        { Assertions.assertEquals(entities[index2], result[2]) },
    )
}

internal fun assertEntityOrder6Items(
    index0: Int,
    index1: Int,
    index2: Int,
    index3: Int,
    index4: Int,
    index5: Int,
    entities: List<StarWarsEntity>,
    result: List<StarWarsEntity>,
) {
    Assertions.assertAll(
        { Assertions.assertEquals(6, result.size) },
        { Assertions.assertEquals(entities[index0], result[0]) },
        { Assertions.assertEquals(entities[index1], result[1]) },
        { Assertions.assertEquals(entities[index2], result[2]) },
        { Assertions.assertEquals(entities[index3], result[3]) },
        { Assertions.assertEquals(entities[index4], result[4]) },
        { Assertions.assertEquals(entities[index5], result[5]) },
    )
}

internal fun assertEntityOrder12Items(
    index0: Int,
    index1: Int,
    index2: Int,
    index3: Int,
    index4: Int,
    index5: Int,
    index6: Int,
    index7: Int,
    index8: Int,
    index9: Int,
    index10: Int,
    index11: Int,
    entities: List<StarWarsEntity>,
    result: List<StarWarsEntity>,
) {
    Assertions.assertAll(
        { Assertions.assertEquals(12, result.size) },
        { Assertions.assertEquals(entities[index0], result[0]) },
        { Assertions.assertEquals(entities[index1], result[1]) },
        { Assertions.assertEquals(entities[index2], result[2]) },
        { Assertions.assertEquals(entities[index3], result[3]) },
        { Assertions.assertEquals(entities[index4], result[4]) },
        { Assertions.assertEquals(entities[index5], result[5]) },
        { Assertions.assertEquals(entities[index6], result[6]) },
        { Assertions.assertEquals(entities[index7], result[7]) },
        { Assertions.assertEquals(entities[index8], result[8]) },
        { Assertions.assertEquals(entities[index9], result[9]) },
        { Assertions.assertEquals(entities[index10], result[10]) },
        { Assertions.assertEquals(entities[index11], result[11]) },
    )
}
