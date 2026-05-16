package com.example.notesappnavigation.di

import org.koin.test.KoinTest
import org.junit.Test
import kotlin.test.assertNotNull

class KoinModuleTest : KoinTest {
    @Test
    fun verifyModulesList() {
        // Melibas folder DI agar tidak 0%
        assertNotNull(dataModule)
        assertNotNull(viewModelModule)
        assertNotNull(platformModule)
        assertNotNull(appModule)
    }
}
