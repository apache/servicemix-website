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

import org.junit.Test
import org.junit.Assert._

import Helper._

/**
 * Test cases for download link generation methods
 */
class HelperTest {

  @Test
  def testDownloadsForRelease() = {
    implicit val release = Release("4.5.0")
    assertEquals("http://www.apache.org/dyn/closer.cgi?path=servicemix/servicemix-4/4.5.0/apache-servicemix-jbi-4.5.0.tar.gz",
                 download(jbi, tarball))
    assertEquals("http://www.apache.org/dyn/closer.cgi?path=servicemix/servicemix-4/4.5.0/apache-servicemix-full-4.5.0.zip",
                 download(full,zip))
    assertEquals("http://www.apache.org/dyn/closer.cgi?path=servicemix/servicemix-4/4.5.0/apache-servicemix-4.5.0-src.zip",
                 download(source,zip))
    assertEquals("http://www.apache.org/dist/servicemix/servicemix-4/4.5.0/apache-servicemix-minimal-4.5.0.zip.asc",
                 pgp(minimal,zip))
  }

  @Test
  def testDownloadsForArchivedRelease() = {
    implicit val release = Release("4.4.0", true)
    assertEquals("http://archive.apache.org/dist/servicemix/servicemix-4/4.4.0/apache-servicemix-jbi-4.4.0.tar.gz",
                 download(jbi, tarball))
    assertEquals("http://archive.apache.org/dist/servicemix/servicemix-4/4.4.0/apache-servicemix-full-4.4.0.zip",
                 download(full,zip))
    assertEquals("http://archive.apache.org/dist/servicemix/servicemix-4/4.4.0/apache-servicemix-4.4.0-src.zip",
                 download(source,zip))
    assertEquals("http://archive.apache.org/dist/servicemix/servicemix-4/4.4.0/apache-servicemix-minimal-4.4.0.zip.sha1",
                 sha1(minimal,zip))
  }

  @Test
  def testDownloadsForWarRelease() = {
    implicit val release = Release("3.4.0", true)
    assertEquals("http://archive.apache.org/dist/servicemix/servicemix-3/3.4.0/apache-servicemix-web-3.4.0.war",
                 download(war))
    assertEquals("http://archive.apache.org/dist/servicemix/servicemix-3/3.4.0/apache-servicemix-web-3.4.0.war.md5",
                 md5(war))
  }

  @Test
  def testDownloadsForSnapshotRelease() = {
    implicit val release = Release("5.0.0-SNAPSHOT")
    assertEquals("http://repository.apache.org/service/local/artifact/maven/redirect?r=snapshots&g=org.apache.servicemix&a=apache-servicemix&v=5.0.0-SNAPSHOT&p=tar.gz",
                 download(tarball))
    assertEquals("http://repository.apache.org/service/local/artifact/maven/redirect?r=snapshots&g=org.apache.servicemix&a=apache-servicemix-full&v=5.0.0-SNAPSHOT&p=zip",
                 download(full,zip))
  }

  @Test
  def testDownloadsForCurrentVersion5Release() = {
    implicit val release = Release("5.0.0")
    assertEquals("http://www.apache.org/dyn/closer.cgi?path=servicemix/servicemix-5/5.0.0/apache-servicemix-5.0.0.zip",
      download(zip))
    assertEquals("http://www.apache.org/dyn/closer.cgi?path=servicemix/servicemix-5/5.0.0/apache-servicemix-5.0.0-src.zip",
      download(source,zip))
  }

  @Test
  def testDocumentationForSnapshotRelease = {
    implicit val release = Release("4.5.2-SNAPSHOT")
    assertEquals("http://servicemix.apache.org/docs/4.5.x/index.html", docs("index.html"))
    assertEquals("http://servicemix.apache.org/docs/4.5.x/quickstart/index.html", docs("quickstart/index.html"))
  }

  @Test
  def testDocumentationForRelease = {
    implicit val release = Release("4.4.2")
    assertEquals("http://servicemix.apache.org/docs/4.4.x/index.html", docs("index.html"))
    assertEquals("http://servicemix.apache.org/docs/4.4.x/quickstart/index.html", docs("quickstart/index.html"))
  }


}
