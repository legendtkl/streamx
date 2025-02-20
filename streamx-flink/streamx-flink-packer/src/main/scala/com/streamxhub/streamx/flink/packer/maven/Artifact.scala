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

package com.streamxhub.streamx.flink.packer.maven

import java.util.regex.Pattern
import org.eclipse.aether.artifact.{Artifact => AetherArtifact}

case class Artifact(groupId: String, artifactId: String, version: String) {

  def eq(artifact: AetherArtifact): Boolean = {
    artifact.getGroupId match {
      case g if g == groupId =>
        artifact.getArtifactId match {
          case "*" => true
          case a => a == artifactId
        }
      case _ => false
    }
  }

}

object Artifact {

  private lazy val PATTERN = Pattern.compile("([^: ]+):([^: ]+):([^: ]+)")

  /**
   * build from coords
   */
  def of(coords: String): Artifact = {
    PATTERN.matcher(coords) match {
      case m if m.matches() =>
        val g = m.group(1)
        val a = m.group(2)
        val v = m.group(3)
        Artifact(g, a, v)
      case _ =>
        throw new IllegalArgumentException(s"Bad artifact coordinates $coords, expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>")
    }
  }
}
