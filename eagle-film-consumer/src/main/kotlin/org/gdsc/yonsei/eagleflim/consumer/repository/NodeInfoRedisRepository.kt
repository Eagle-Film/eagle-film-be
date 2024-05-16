package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
import org.springframework.data.repository.CrudRepository

interface NodeInfoRedisRepository: CrudRepository<NodeInfo, String> {
}
