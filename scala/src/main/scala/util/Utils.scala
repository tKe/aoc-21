package util

import cats.effect.{Resource, Sync}

import scala.io.{BufferedSource, Source}

object Utils {
  def readFile[F[_]: Sync](path: String): Resource[F, BufferedSource] =
    Resource.make(Sync[F].blocking(Source.fromFile(path))) { file => Sync[F].blocking(file.close()) }

  def readLines[F[_]: Sync](path: String): F[Seq[String]] = {
    readFile[F](path).use(f => Sync[F].blocking(f.getLines().toSeq))
  }
}
