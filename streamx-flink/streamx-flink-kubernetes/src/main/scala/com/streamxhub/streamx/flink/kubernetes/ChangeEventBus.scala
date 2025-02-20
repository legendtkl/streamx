/*
 * Copyright 2019 The StreamX Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.streamxhub.streamx.flink.kubernetes

import com.google.common.eventbus.{AsyncEventBus, EventBus}

import java.util.concurrent.{LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}

/**
 * @author Al-assad
 */
// noinspection UnstableApiUsage
class ChangeEventBus {

  private val execPool = new ThreadPoolExecutor(6, 12,
    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue[Runnable](10000))

  private[kubernetes] val asyncEventBus = new AsyncEventBus("[StreamX][flink-k8s]AsyncEventBus", execPool)

  private[kubernetes] val syncEventBus = new EventBus("[StreamX][flink-k8s]SyncEventBus")

  def postAsync(event: AnyRef): Unit = asyncEventBus.post(event)

  def postSync(event: AnyRef): Unit = syncEventBus.post(event)

  def registerListener(listener: AnyRef): Unit = {
    asyncEventBus.register(listener)
    syncEventBus.register(listener)
  }


}
