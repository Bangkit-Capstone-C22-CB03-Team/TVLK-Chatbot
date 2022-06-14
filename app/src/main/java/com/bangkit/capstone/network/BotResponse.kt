package com.bangkit.capstone.network

import com.google.gson.annotations.SerializedName

data class BotResponse(

	@field:SerializedName("answer")
	val botResponse: String
)
