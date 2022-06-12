package team.codemonsters.ddd.toolkit.controller

import java.io.Serializable

data class RestError(val errorMessage: String, val code: String) : Serializable
