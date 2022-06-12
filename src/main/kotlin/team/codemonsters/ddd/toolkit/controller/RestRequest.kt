package team.codemonsters.ddd.toolkit.controller

import java.io.Serializable

data class RestRequest<T> (val data:T) : Serializable