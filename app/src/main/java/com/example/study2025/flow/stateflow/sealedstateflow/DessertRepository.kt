package com.example.study2025.flow.stateflow.sealedstateflow

import javax.inject.Inject

class DessertRepository @Inject constructor(){
    suspend fun getDesert(): List<String> {
        return listOf("Donut", "Cupcake", "Brownie", "Cheesecake")
    }
}