package stock.monitoring.service.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Movie(@BsonId val id: ObjectId, val title: String, val genres: List<String>)
