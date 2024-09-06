package kz.nikitos.hackingthebookshelf.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = ReplySerializer::class)
sealed interface TestModel {
    // {"error": "error"}
    @Serializable
    data class Error(val error: String) : TestModel

    // {"spam":"egg","something":"some"}
    @Serializable
    data class NotError(val spam: String, val something: String) : TestModel
}

object ReplySerializer : JsonContentPolymorphicSerializer<TestModel>(TestModel::class) {
    override fun selectDeserializer(content: JsonElement) = when {
        "error" in content.jsonObject -> TestModel.Error.serializer()
        else -> TestModel.NotError.serializer()
    }
}