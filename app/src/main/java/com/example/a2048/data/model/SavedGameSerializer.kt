package com.example.a2048.data.model

import androidx.datastore.core.Serializer
import com.example.a2048.SavedGameProto
import java.io.InputStream
import java.io.OutputStream

object SavedGameSerializer :Serializer<SavedGameProto> {
    override val defaultValue: SavedGameProto= SavedGameProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SavedGameProto {
        try {
           return SavedGameProto.parseFrom(input)
        }catch (exception:Exception){
            throw exception
        }
    }

    override suspend fun writeTo(t: SavedGameProto, output: OutputStream) =t.writeTo(output)


}

