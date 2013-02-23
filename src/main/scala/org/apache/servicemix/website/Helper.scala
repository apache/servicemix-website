/**
 * Copyright (C) 2009-2013 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.apache.servicemix.website

/**
 * Helper methods to build the download pages
 */
object Helper {

  /**
   * Generate a download url for a release/snapshot artifact
   */
  def download(specs: (ReleaseArtifact => ReleaseArtifact)*)(implicit release: Release) : String = {
    artifactFor(specs, release) match {
      case artifact @ ReleaseArtifact(release, _, _, _) if isSnapshot(release) => _snapshot(artifact)
      case artifact @ ReleaseArtifact(release, _, _, _) if release.archived => _archive(artifact)
      case artifact => _mirror(artifact)
    }
  }

  /**
   * Generate the URL fo the MD5 hash
   */
  def md5(specs: (ReleaseArtifact => ReleaseArtifact)*)(implicit release: Release) : String = _metafile(artifactFor(specs, release), "md5")

  /**
   * Generate the URL fo the PGP signatur
   */
  def pgp(specs: (ReleaseArtifact => ReleaseArtifact)*)(implicit release: Release) : String = _metafile(artifactFor(specs, release), "asc")

  /**
   * Generate the URL fo the SHA1 hash
   */
  def sha1(specs: (ReleaseArtifact => ReleaseArtifact)*)(implicit release: Release) : String = _metafile(artifactFor(specs, release), "sha1")

  /**
   * Function to specify a -minimal assembly
   */
  val minimal : ReleaseArtifact => ReleaseArtifact = (artifact) => artifact match {
    case ReleaseArtifact(release, _, packaging, classifier) => ReleaseArtifact(release, "apache-servicemix-minimal", packaging, classifier)
  }

  /**
   * Function to specify a -jbi assembly
   */
  val jbi : ReleaseArtifact => ReleaseArtifact = (artifact) => artifact match {
    case ReleaseArtifact(release, _, packaging, classifier) => ReleaseArtifact(release, "apache-servicemix-jbi", packaging, classifier)
  }

  /**
   * Function to specify a -full assembly
   */
  val full : ReleaseArtifact => ReleaseArtifact = (artifact) => artifact match {
    case ReleaseArtifact(release, _, packaging, classifier) => ReleaseArtifact(release, "apache-servicemix-full", packaging, classifier)
  }

  /**
   * Function to specify a -src assembly
   */
  val source : ReleaseArtifact => ReleaseArtifact = (artifact) => artifact match {
    case ReleaseArtifact(release, artifact, packaging, _) => ReleaseArtifact(release, artifact, packaging, Some("src"))
  }

  /**
   * Function to specify tar.gz packaging
   */
  val tarball : ReleaseArtifact => ReleaseArtifact = (artifact) => artifact match {
    case ReleaseArtifact(release, artifact, _, classifier) => ReleaseArtifact(release, artifact, "tar.gz", classifier)
  }

  /**
   * Function to specify zip packaging
   */
  val zip : ReleaseArtifact => ReleaseArtifact = (artifact) => artifact match {
    case ReleaseArtifact(release, artifact, _, classifier) => ReleaseArtifact(release, artifact, "zip", classifier)
  }

  /**
   * Function to specify war packaging
   */
  val war : ReleaseArtifact => ReleaseArtifact = (artifact) => artifact match {
    case ReleaseArtifact(release, _, _, classifier) => ReleaseArtifact(release, "apache-servicemix-web", "war", classifier)
  }


  val isSnapshot : Release => Boolean = _.version.endsWith("SNAPSHOT")
  val isRelease : Release => Boolean = !isSnapshot(_)

  val filename : ReleaseArtifact => String = _ match {
    case ReleaseArtifact(release, artifact, packaging, Some(classifier)) => s"${artifact}-${release.version}-${classifier}.${packaging}"
    case ReleaseArtifact(release, artifact, packaging, None) => s"${artifact}-${release.version}.${packaging}"
  }

  val location : ReleaseArtifact => String = _ match {
    case ReleaseArtifact(Release(version, _), _, _, _) if version.startsWith("3") => "servicemix-3"
    case _ => "servicemix-4"
  }

  val versionOf : ReleaseArtifact => String = _.release.version

  val artifactFor : (Seq[ReleaseArtifact => ReleaseArtifact], Release) => ReleaseArtifact =
    (specs, release) => specs.foldLeft(ReleaseArtifact(release))((artifact, spec) => spec(artifact))

  private def _metafile(artifact: ReleaseArtifact, suffix: String) : String =
    artifact match {
      case ReleaseArtifact(release, _, _, _) if release.archived => s"${_archive(artifact)}.${suffix}"
      case _ => s"${_dist(artifact)}.${suffix}"
    }

  private def _archive(artifact: ReleaseArtifact) : String =
    s"http://archive.apache.org/dist/servicemix/${location(artifact)}/${versionOf(artifact)}/${filename(artifact)}"

  private def _dist(artifact: ReleaseArtifact) : String = {
    s"http://www.apache.org/dist/servicemix/${location(artifact)}/${artifact.release.version}/${filename(artifact)}"
  }

  private def _mirror(artifact: ReleaseArtifact) : String =
    s"http://www.apache.org/dyn/closer.cgi?path=servicemix/${location(artifact)}/${artifact.release.version}/${filename(artifact)}"

  private def _snapshot(artifact: ReleaseArtifact) : String =
    s"http://repository.apache.org/service/local/artifact/maven/redirect?r=snapshots&g=org.apache.servicemix&a=${artifact.artifact}&v=${versionOf(artifact)}&p=${artifact.packaging}"

  case class ReleaseArtifact(release: Release,
                             artifact: String = "apache-servicemix",
                             packaging: String = "tar.gz",
                             classifier: Option[String] = None)

}

/**
 * Defines a version of Apache ServiceMix
 *
 * @param version the version
 * @param archived <code>true<code> if the version is no longer available on the download mirrors
 */
case class Release(version: String, archived: Boolean = false)
