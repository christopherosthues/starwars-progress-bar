package com.christopherosthues.starwarsprogressbar.mocks

import com.intellij.ide.util.PropertiesComponent
import org.jetbrains.annotations.NonNls

internal class TestPropertiesComponent : PropertiesComponent() {
    private val values = mutableMapOf<String, String?>()

    override fun unsetValue(name: String) {
        values.remove(name)
    }

    override fun getValue(name: String): String? {
        return values[name]
    }

    override fun setValue(name: String, value: String?) {
        values[name] = value
    }

    override fun setValue(name: String, value: String?, defaultValue: String?) {
    }

    override fun setValue(name: String, value: Float, defaultValue: Float) {
    }

    override fun setValue(name: String, value: Int, defaultValue: Int) {
    }

    override fun setValue(name: String, value: Boolean, defaultValue: Boolean) {
        values[name] = value.toString()
    }

    override fun getValues(p0: @NonNls String): Array<out String?>? {
        return null
    }

    override fun setValues(
        p0: @NonNls String,
        p1: Array<out String?>?
    ) {
    }

    override fun getList(p0: @NonNls String): List<String?>? {
        return listOf<String?>()
    }

    override fun setList(
        p0: @NonNls String,
        p1: Collection<String?>?
    ) {
    }

    override fun updateValue(
        p0: @NonNls String,
        p1: Boolean
    ): Boolean {
        return false
    }

    override fun isValueSet(name: String): Boolean {
        return values.containsKey(name)
    }
}
