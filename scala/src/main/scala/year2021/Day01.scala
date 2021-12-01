package year2021

import cats.effect.{IO, IOApp}
import cats.implicits._
import util.Utils._

object Day01 extends IOApp.Simple {
  val run: IO[Unit] = for {
    inputFile <- IO.pure("../aoc-21-inputs/year-2021/day-01/input.txt")
    lines <- readLines[IO](inputFile)
    result1 = part1(lines)
    _ <- IO.println(s"Part1: $result1")
    result2 = part2(lines)
    _ <- IO.println(s"Part2: $result2")
  } yield ()

  def part1(lines: Seq[String]): Int = lines.map(_.toInt)
    .sliding(2).count { case Seq(a, b) => a < b }

  def part2(lines: Seq[String]): Int = lines.map(_.toInt)
    .sliding(3).map(_.sum)
    .sliding(2).count { case Seq(a, b) => a < b }
}



