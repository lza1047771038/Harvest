package com.harvest.core_network.factory

import com.google.gson.Gson
import com.harvest.core_network.wrapper.ApiResult
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ConverterFactory private constructor() : Converter.Factory() {

    companion object {
        @JvmStatic
        fun create(): ConverterFactory {
            return ConverterFactory()
        }
    }


    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type is ParameterizedType) {
            val rawType = type.rawType
            if (rawType == ApiResult::class.java) {
                val actualTypes = type.actualTypeArguments
                if (actualTypes.size == 1 && actualTypes[0] == JSONObject::class.java) {
                    return JsonObjectConverter()
                } else if (actualTypes.size == 1) {
                    return GsonConverter(actualTypes[0])
                }
            }
        }
        return null
    }
}

class JsonObjectConverter : Converter<ResponseBody, ApiResult<JSONObject>> {
    override fun convert(value: ResponseBody): ApiResult<JSONObject> {
        val string = value.string()
        val jsonObject = JSONObject(string)
        val code = jsonObject.optInt("code")
        val message = jsonObject.optString("message")
        return ApiResult<JSONObject>(code = code, message = message, data = jsonObject)
    }
}

class GsonConverter(val type: Type) : Converter<ResponseBody, ApiResult<Any>> {
    private val gson = Gson()
    override fun convert(value: ResponseBody): ApiResult<Any> {
        val string = value.string()
        val obj = gson.fromJson<Any>(string, type)
        return ApiResult<Any>(data = obj)
    }
}