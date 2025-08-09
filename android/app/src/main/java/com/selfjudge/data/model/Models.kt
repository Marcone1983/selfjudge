package com.selfjudge.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null
)

data class BootstrapResponse(
    val ok: Boolean? = null,
    val already: Boolean? = null,
    val credits: Credits? = null
)

data class Credits(
    val solo: Int,
    val pvp: Int
)

data class JudgeRequest(
    val artist: String,
    val text: String
)

data class BattleRequest(
    val artist: String,
    @SerializedName("text_a") val textA: String,
    @SerializedName("text_b") val textB: String
)

data class Judgment(
    val persona: String,
    val score: Int,
    val comment: String
)

data class JudgeResponse(
    val artist: String,
    val judgments: List<Judgment>,
    val average: Float
)

data class BattleResponse(
    val artist: String,
    @SerializedName("A") val judgeA: JudgeData,
    @SerializedName("B") val judgeB: JudgeData,
    @SerializedName("avgA") val averageA: Float,
    @SerializedName("avgB") val averageB: Float,
    val winner: String
)

data class JudgeData(
    val judgments: List<Judgment>
)

data class ApiError(
    val error: String,
    val detail: String? = null
)