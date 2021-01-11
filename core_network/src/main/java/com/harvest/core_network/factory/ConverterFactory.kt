package com.harvest.core_network.factory

import com.google.gson.JsonObject
import com.harvest.core_network.wrapper.ApiResult
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ConverterFactory : Converter.Factory {

    companion object{
        @JvmStatic
        fun create():ConverterFactory{
            return ConverterFactory()
        }
    }


    private constructor():super()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type is ParameterizedType) {
            val rawType = type.rawType
            if (rawType == ApiResult::class.java) {
                val actualTypes = type.actualTypeArguments
                if (actualTypes.size == 1 && actualTypes[0] === JSONObject::class.java) {
                    return GsonConverter()
                }
            }
        }
        return null
    }
}

class GsonConverter : Converter<ResponseBody, ApiResult<JSONObject>> {
    override fun convert(value: ResponseBody): ApiResult<JSONObject>? {
        val jsonObject = JSONObject(value.string())
        val code = jsonObject.optInt("code")
        val message = jsonObject.optString("message")
        return ApiResult<JSONObject>(code = code, message = message, data = jsonObject)
    }
}