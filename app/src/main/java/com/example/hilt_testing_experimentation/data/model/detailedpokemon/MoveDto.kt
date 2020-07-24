package com.example.hilt_testing_experimentation.data.model.detailedpokemon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MoveDto : Serializable {
    @SerializedName("move")
    @Expose
    var move: MoveDto_? = null
}